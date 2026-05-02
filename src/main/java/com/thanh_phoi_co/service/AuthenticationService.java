package com.thanh_phoi_co.service;

import com.thanh_phoi_co.dto.request.ChangePasswordRequest;
import com.thanh_phoi_co.dto.request.LoginRequest;
import com.thanh_phoi_co.dto.request.SignUpRequest;
import com.thanh_phoi_co.dto.response.*;
import com.thanh_phoi_co.enums.Roles;
import com.thanh_phoi_co.enums.TokenType;
import com.thanh_phoi_co.enums.UserStatus;
import com.thanh_phoi_co.exception.AppException;
import com.thanh_phoi_co.exception.ErrorCode;
import com.thanh_phoi_co.exception.InvalidDataException;
import com.thanh_phoi_co.mapper.UserMapper;
import com.thanh_phoi_co.model.Role;
import com.thanh_phoi_co.model.Token;
import com.thanh_phoi_co.model.User;
import com.thanh_phoi_co.repository.RoleRepository;
import com.thanh_phoi_co.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    public TokenResponse authenticate(LoginRequest request) {

        log.info("-------- authenticate --------");

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            // Sai tài khoản hoặc mật khẩu
            throw new InvalidDataException("Username or password incorrect");
        }

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidDataException("User not found"));

        // Check trạng thái tài khoản
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new AppException(ErrorCode.USER_INACTIVE);
        }

        // Generate JWT
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        tokenService.save(
                Token.builder()
                        .username(user.getUsername())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(Role::getName)
                                .collect(java.util.stream.Collectors.toSet())
                )
                .build();
    }

    public AccessTokenResponse refresh(HttpServletRequest request) {

        log.info("--------------refreshToken----------------");

        String authHeader = request.getHeader(AUTHORIZATION);

        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new InvalidDataException("Refresh token must be provided");
        }

        String refreshToken = authHeader.substring(7);

        final String userName = jwtService.extractUsername(
                refreshToken,
                TokenType.REFRESH_TOKEN
        );

        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new InvalidDataException("User not found"));

        if (!jwtService.isValid(refreshToken, TokenType.REFRESH_TOKEN, user)) {
            throw new InvalidDataException("Token is invalid");
        }

        String accessToken = jwtService.generateToken(user);

        tokenService.save(Token.builder()
                .username(userName)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());

        return AccessTokenResponse.builder()
                .accessToken(accessToken)
                .userId(user.getId())
                .build();
    }
    public String logout(HttpServletRequest request){
        log.info("-------logout---------");

        String refresh_token = request.getHeader(AUTHORIZATION);
        if(StringUtils.isBlank(refresh_token)){
            throw new InvalidDataException("Token must be not blank");
        }

        final String username = jwtService.extractUsername(refresh_token, TokenType.REFRESH_TOKEN);
        Optional<User> user = userRepository.findByUsername(username);

        user.get().setLastLogin(LocalDateTime.now());
        //check token in db
        Token currentToken = tokenService.getByUsername(username);

        //delete token
        tokenService.delete(currentToken);
        return "Logout Successfully";
    }


    public ForgotPasswordResponse forgotPassword(String email) {
        log.info("------ forgotPassword ------");

        User user = userService.getByEmail(email);

        if (!user.isEnabled()) {
            throw new InvalidDataException("User not active");
        }

        //generate token
        String resetToken = jwtService.generateResetToken(user);

        // FE link
        String resetLink = String.format(
                "http://localhost:3000/reset-password?token=%s",
                resetToken
        );

        // gửi mail
        emailService.sendResetPasswordEmail(user.getEmail(), resetLink);

        log.info("Reset password email sent to {}", user.getEmail());

        return ForgotPasswordResponse.builder()
                .message("Reset password email sent successfully")
                .build();
    }


    public IntrospectResponse validateResetToken(String resetKey) {
        log.info("--------------------validateResetToken---------------");

        // 1. validate input
        if (resetKey == null || resetKey.isBlank()) {
            throw new InvalidDataException("Reset token must not be blank");
        }

        try {
            // 2. extract username từ token
            final String username = jwtService.extractUsername(resetKey, TokenType.RESET_TOKEN);

            // 3. lấy user
            var user = userService.getByUsername(username);

            // 4. check user active
            if (!user.isEnabled()) {
                throw new InvalidDataException("User not active");
            }

            // 5. validate token
            if (!jwtService.isValid(resetKey, TokenType.RESET_TOKEN, user)) {
                throw new InvalidDataException("Token is invalid or expired");
            }

            // 6. hợp lệ
            return IntrospectResponse.builder()
                    .valid(true)
                    .build();

        } catch (Exception e) {
            throw new InvalidDataException("Invalid or expired reset token");
        }
    }


    public UserResponse signUp(SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new InvalidDataException("Username already exists");
        }

         if (userRepository.existsByEmail(request.getEmail())) {
             throw new InvalidDataException("Email already exists");
         }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidDataException("Password does not match");
        }

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository.findByName(Roles.USER.name())
                .orElseThrow(() -> new InvalidDataException("Role USER does not exist"));

        user.setRoles(new HashSet<>(Set.of(userRole)));

        userRepository.save(user);

        log.info("User sign up successfully: {}", user.getUsername());

        return userMapper.toUserResponse(user);
    }

    public String changePassword(ChangePasswordRequest request) {
        User user = isValidUserByToken(request.getSecretKey());
        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new InvalidDataException("Password not match");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return "Changed Password successfully!!!";
    }

    public User isValidUserByToken(String secretKey) {
        final String username = jwtService.extractUsername(secretKey, TokenType.RESET_TOKEN);
        var user = userService.getByUsername(username);

        if (!user.isEnabled()) {
            throw new InvalidDataException("User not active");
        }

        if (!jwtService.isValid(secretKey, TokenType.RESET_TOKEN, user)) {
            throw new InvalidDataException("Not allow access with this token");
        }

        return user;
    }

}

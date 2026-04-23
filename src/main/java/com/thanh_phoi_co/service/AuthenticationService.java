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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
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
            // ❗ sai username/password
            throw new InvalidDataException("Username or password incorrect");
        }

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidDataException("User not found"));

        // ❗ check status
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new AppException(ErrorCode.USER_INACTIVE);
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        user.setLastLogin(new Date());
        userRepository.save(user);

        // save token
        tokenService.save(Token.builder()
                .username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    public AccessTokenResponse refresh(HttpServletRequest request) throws Exception {

        log.info("--------------refreshToken----------------");
        //validate
        String refresh_token = request.getHeader(AUTHORIZATION);

        if(StringUtils.isBlank(refresh_token)){
            throw new InvalidDataException("Token must be not blank");
        }

        //extract user from token
        final String userName = jwtService.extractUsername(refresh_token, TokenType.REFRESH_TOKEN);

        //check is into db
        Optional<User> user = userRepository.findByUsername(userName);

        if(!jwtService.isValid(refresh_token, TokenType.REFRESH_TOKEN , user.get())){
            throw new InvalidDataException("Token is invalid");
        }

        String access_token = jwtService.generateToken(user.get());

        tokenService.save(Token.builder()
                .username(userName)
                .accessToken(access_token)
                .refreshToken(refresh_token)
                .build());

        return AccessTokenResponse.builder()
                .accessToken(access_token)
                .userId(user.get().getId())
                .build();
    }
    public String logout(HttpServletRequest request) throws Exception {
        log.info("-------logout---------");

        String refresh_token = request.getHeader(AUTHORIZATION);
        if(StringUtils.isBlank(refresh_token)){
            throw new InvalidDataException("Token must be not blank");
        }

        final String username = jwtService.extractUsername(refresh_token, TokenType.REFRESH_TOKEN);
        Optional<User> user = userRepository.findByUsername(username);

        user.get().setLastLogin(new Date());
        //check token in db
        Token currentToken = tokenService.getByUsername(username);

        //delete token
        tokenService.delete(currentToken);
        return "Logout Successfully";
    }


    public ForgotPasswordResponse forgotPassword(String email) throws Exception {
        //check email
        User user = userService.getByEmail(email);

        //User is active or inactive
        if(!user.isEnabled()){
            throw new InvalidDataException("User not active");
        }

        //generate resetToken
        String resetToken = jwtService.generateResetToken(user);

        //send email confirmLink
        String confirmLink = String.format("curl --location 'localhost:9001/auth/reset-password' \\\n" +
                "--header 'Content-Type: application/json' \\\n" +
                "--header 'Accept: */*' \\\n" +
                "--header 'Cookie: JSESSIONID=F8082D07E3C74EAD1619C524CE3F8BD1' \\\n" +
                "--data '{\n" +
                "  \"token\": \"%s\"\n" +
                "}'", resetToken);
        log.info("confirmLink= {}",confirmLink);

        return ForgotPasswordResponse.builder()
                .resetToken(resetToken)
                .build();
    }


    public IntrospectResponse resetPassword(String resetKey) throws Exception {
        log.info("--------------------resetPassword---------------");

        final String username = jwtService.extractUsername(resetKey,TokenType.RESET_TOKEN);

        var user = userService.getByUsername(username);
        boolean isValid = true;

        if(!jwtService.isValid(resetKey,TokenType.RESET_TOKEN, user)) {
            throw new InvalidDataException("Not allow access with this token");
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
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

    public String changePassword(ChangePasswordRequest request) throws Exception {
        User user = isValidUserByToken(request.getSecretKey());
        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new InvalidDataException("Password not match");

        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return "Changed Password successfully!!!";
    }

    public User isValidUserByToken(String secretKey) throws Exception {
        final String username = jwtService.extractUsername(secretKey,TokenType.RESET_TOKEN);
        var user = userService.getByUsername(username);

        if(!user.isEnabled()){
            throw new InvalidDataException("User not active");
        }

        if(!jwtService.isValid(secretKey, TokenType.RESET_TOKEN,user)){
            throw  new InvalidDataException("Not allow access with this token");
        }

        return user;
    }

}

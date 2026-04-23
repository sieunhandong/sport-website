package com.thanh_phoi_co.service.impl;

import com.thanh_phoi_co.configuration.JwtProvider;
import com.thanh_phoi_co.enums.TokenType;
import com.thanh_phoi_co.exception.AppException;
import com.thanh_phoi_co.exception.ErrorCode;
import com.thanh_phoi_co.exception.InvalidDataException;
import com.thanh_phoi_co.repository.UserRepository;
import com.thanh_phoi_co.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    @Value("${jwt.privateKey}")
    private String privateKey;

    @Value("${jwt.expiryHour}")
    private Long expiryHour;

    @Value("${jwt.expiryDay}")
    private Long expiryDay;
    @Override
    public String generateToken(UserDetails user) {
        try {
            return generateToken(new HashMap<>(), user);
        } catch (Exception e) {
            throw new AppException(ErrorCode.TOKEN_GENERATION_FAILED);
        }
    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        try {
            return generateRefreshToken(new HashMap<>(), user);
        } catch (Exception e) {
            throw new AppException(ErrorCode.TOKEN_GENERATION_FAILED);
        }
    }

    @Override
    public String generateResetToken(UserDetails user) throws Exception {
        return generateResetToken(new HashMap<>(),user);
    }

    @Override
    public String extractUsername(String token, TokenType type) throws Exception {
        return extractClaim(token,type, Claims::getSubject);
    }

    @Override
    public boolean isValid(String token, TokenType type, UserDetails userDetails) throws Exception {
        final String username = extractUsername(token, type);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token,type));
    }


    // giai ma token
    private Claims extraAllClaim(String token, TokenType type) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(getPublicKey(type)) //cung cap khoa bi mat de giai ma
                .build()
                .parseClaimsJws(token)//giai ma token lay thong tin tu claim
                .getBody(); //tra ve cac claim cua token
    }

    //truy xuat 1 claim tu token ca tra ve voi bat ky gia tri nao cua claim
    private  <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimResolver) throws Exception {
        final Claims claims = extraAllClaim(token, type);
        return claimResolver.apply(claims);
    }

    private boolean isTokenExpired(String token, TokenType type) throws Exception {
        return extractExpiration(token, type).before(new Date());
    }
    private Date extractExpiration(String token, TokenType type) throws Exception {
        return extractClaim(token,type , Claims::getExpiration);
    }
    @Override
    public Date extractLastActivity(String token, TokenType type) throws Exception {
        return extractClaim(token, type,claims ->(Date) claims.get("lastActivity"));
    }

    private String generateToken(Map<String, Object> claims, UserDetails userDetails) throws Exception{
        return Jwts.builder()
                .setClaims(claims)
                .claim("scope", buildScope(userDetails))  // Đảm bảo claims được thêm vào đúng cách
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expiryHour))  // Đặt thời gian hết hạn
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.RS512)  // Ký JWT với private key và RSA512
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails)throws Exception{
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryDay))
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.RS512)  // Ký JWT với private key và RSA512
                .compact();
    }

    private String generateResetToken(Map<String, Object> claims, UserDetails userDetails) throws Exception {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(getKey(TokenType.RESET_TOKEN), SignatureAlgorithm.RS512)
                .compact();
    }

    private PrivateKey getKey(TokenType type) throws Exception {
        switch (type) {
            case ACCESS_TOKEN -> {
                return jwtProvider.getPrivateKey();
            }
            case REFRESH_TOKEN -> {
                return jwtProvider.getPrivateKey();
            }
            case RESET_TOKEN -> {
                return jwtProvider.getPrivateKey();
            }
            default -> throw new InvalidDataException("Token type invalid");
        }
    }
    private PublicKey getPublicKey(TokenType type) throws Exception {
        switch (type) {
            case ACCESS_TOKEN -> {
                return jwtProvider.getPublicKey();
            }
            case REFRESH_TOKEN -> {
                return jwtProvider.getPublicKey();
            }
            case RESET_TOKEN -> {
                return jwtProvider.getPublicKey();
            }
            default -> throw new InvalidDataException("Token type invalid");
        }
    }


    @Override
    public String extractScope(String token, TokenType type) throws Exception {
        return extractClaim(token,type,claims -> claims.get("scope", String.class));
    }

    private String buildScope(UserDetails user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        // Lấy danh sách vai trò từ repository
        List<String> roles = userRepository.findRolesByUsername(user.getUsername());

        // Nếu danh sách vai trò không rỗng, thêm vai trò vào StringJoiner
        if (!roles.isEmpty()) {
            for (String role : roles) {
                log.info("role: {}", role);
                if (role != null) {
                    stringJoiner.add("ROLE_" + role);
                }
            }
        }

        return stringJoiner.toString();
    }
}

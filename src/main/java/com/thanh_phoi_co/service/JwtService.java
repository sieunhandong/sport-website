package com.thanh_phoi_co.service;

import com.thanh_phoi_co.enums.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {
    String generateToken(UserDetails user);

    String generateRefreshToken(UserDetails user);
    String generateResetToken (UserDetails user) throws Exception;

    String extractUsername(String token, TokenType type) throws Exception;

    boolean isValid(String token, TokenType type, UserDetails userDetails) throws Exception;

    Date extractLastActivity(String token, TokenType type) throws Exception;

    String extractScope(String token, TokenType type) throws Exception;

}

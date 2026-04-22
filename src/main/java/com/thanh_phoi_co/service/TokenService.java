package com.thanh_phoi_co.service;

import com.thanh_phoi_co.exception.ResourceNotFoundException;
import com.thanh_phoi_co.model.Token;
import com.thanh_phoi_co.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@Slf4j
public record TokenService(TokenRepository tokenRepository) {
    public String save(Token token){
        Optional<Token> optional = tokenRepository.findByUsername(token.getUsername());
        if(optional.isEmpty()){
            tokenRepository.save(token);
            return token.getId();
        } else {
            Token currentToken = optional.get();
            currentToken.setAccessToken(token.getAccessToken());
            currentToken.setRefreshToken(token.getRefreshToken());
            tokenRepository.save(currentToken);
            return currentToken.getId();
        }

    }
    public String delete(Token token){
        tokenRepository.delete(token);
        return "Deleted token successfully";
    }

    public Token getByUsername(String username){
        return tokenRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("Token not exists"));
    }

}

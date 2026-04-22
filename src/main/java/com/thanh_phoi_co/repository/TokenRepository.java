package com.thanh_phoi_co.repository;

import com.thanh_phoi_co.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByUsername(String username);

}

package com.thanh_phoi_co.configuration;

import com.thanh_phoi_co.exception.AppException;
import com.thanh_phoi_co.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@Slf4j
public class JwtProvider {
    @Value("${jwt.privateKey}")
    private String privateKeyStr;

    @Value("${jwt.publicKey}")
    private String publicKeyStr;

    public PrivateKey getPrivateKey() {
        try {
            String privateKeyPEM = privateKeyStr
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_PUBLIC_KEY);
        } catch (Exception e) {
            throw new AppException(ErrorCode.KEY_PARSE_FAILED);
        }
    }

    public PublicKey getPublicKey() {
        try {
            String publicKeyPEM = publicKeyStr
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePublic(keySpec);

        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_PUBLIC_KEY);
        } catch (Exception e) {
            throw new AppException(ErrorCode.KEY_PARSE_FAILED);
        }
    }
}

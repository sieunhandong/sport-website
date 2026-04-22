package com.thanh_phoi_co.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    PASSWORD_INCORRECT(400, "Password incorrect",HttpStatus.BAD_REQUEST),
    USER_INACTIVE(400,"Your account is inactive",HttpStatus.BAD_REQUEST);
    private int status;
    private String message;
    private HttpStatusCode statusCode;
}

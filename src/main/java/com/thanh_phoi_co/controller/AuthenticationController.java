package com.thanh_phoi_co.controller;

import com.thanh_phoi_co.dto.request.LoginRequest;
import com.thanh_phoi_co.dto.response.ResponseData;
import com.thanh_phoi_co.dto.response.ResponseError;
import com.thanh_phoi_co.dto.response.TokenResponse;
import com.thanh_phoi_co.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
@Tag(name = "Authentication Controller")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "API login")
    @PostMapping("/login")
    public ResponseData<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        try{
            TokenResponse tokenResponse = authenticationService.authenticate(loginRequest);
            return new ResponseData<>(HttpStatus.OK.value(), "Login successful", tokenResponse);
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Username or Password incorrect");
        }
    }
}

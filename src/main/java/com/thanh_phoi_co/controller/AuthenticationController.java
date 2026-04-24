package com.thanh_phoi_co.controller;

import com.thanh_phoi_co.dto.request.ChangePasswordRequest;
import com.thanh_phoi_co.dto.request.ForgetPasswordRequest;
import com.thanh_phoi_co.dto.request.LoginRequest;
import com.thanh_phoi_co.dto.request.SignUpRequest;
import com.thanh_phoi_co.dto.response.*;
import com.thanh_phoi_co.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Tag(name = "Authentication Controller")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "API login")
    @PostMapping("/login")
    public ResponseData<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseData.<TokenResponse>builder()
                .status(200)
                .message("Login successful")
                .data(authenticationService.authenticate(request))
                .build();
    }

    @Operation(summary = "API Sign up")
    @PostMapping("/sign-up")
    public ResponseData<UserResponse> signUp(@Valid @RequestBody SignUpRequest request){
        return ResponseData.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .data(authenticationService.signUp(request))
                .message("Sign Up success")
                .build();
    }

    @Operation(summary = "API Change Password")
    @PostMapping("/change-password")
    public ResponseData<String> changePassword( @Valid @RequestBody ChangePasswordRequest request){
        return ResponseData.<String>builder()
                .status(HttpStatus.OK.value())
                .data(authenticationService.changePassword(request))
                .build();
    }

    @Operation(summary = "API yêu cầu forgot-password")
    @PostMapping("/forgot-password")
    public ResponseData<ForgotPasswordResponse> forgotPassword(
            @Valid @RequestBody ForgetPasswordRequest request) {

        ForgotPasswordResponse response = authenticationService.forgotPassword(request.getEmail());

        return ResponseData.<ForgotPasswordResponse>builder()
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
    }

}

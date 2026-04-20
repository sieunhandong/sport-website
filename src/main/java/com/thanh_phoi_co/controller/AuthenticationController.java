package com.thanh_phoi_co.controller;

import com.thanh_phoi_co.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
@Tag(name = "Authentication Controller")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
}

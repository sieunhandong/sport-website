package com.thanh_phoi_co.controller;

import com.thanh_phoi_co.dto.response.ResponseData;
import com.thanh_phoi_co.dto.response.UserResponse;
import com.thanh_phoi_co.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @Operation(summary = "API get user info")
    @GetMapping("/profile")
    public ResponseData<UserResponse> getUserInfo() {
        UserResponse userResponse = userService.getMyInfo();
        return ResponseData.<UserResponse>builder()
                .status(200)
                .message("Lấy thông tin cá nhân thành công")
                .data(userResponse)
                .build();
    }
}

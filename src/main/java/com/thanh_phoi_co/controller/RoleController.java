package com.thanh_phoi_co.controller;

import com.thanh_phoi_co.dto.request.UpdateUserRoleRequest;
import com.thanh_phoi_co.dto.response.ResponseData;
import com.thanh_phoi_co.dto.response.RoleResponse;
import com.thanh_phoi_co.dto.response.UserResponse;
import com.thanh_phoi_co.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@Validated
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Role Controller")
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "Lấy danh sách tất cả role")
    @GetMapping
    public ResponseData<List<RoleResponse>> getAllRole() {

        List<RoleResponse> response = roleService.getAllRole();

        return ResponseData.<List<RoleResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
    }

    @Operation(summary = "Cập nhật role cho user")
    @PostMapping("/update-user-role")
    public ResponseData<UserResponse> updateUserRole(
            @Valid @RequestBody UpdateUserRoleRequest request) {

        UserResponse response = roleService.updateUserRole(request);

        return ResponseData.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Update user role successfully")
                .data(response)
                .build();
    }
}

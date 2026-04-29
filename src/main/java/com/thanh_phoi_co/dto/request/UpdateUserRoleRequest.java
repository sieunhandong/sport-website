package com.thanh_phoi_co.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRoleRequest {
    private String username;

    @NotEmpty(message = "Role list must not be empty")
    private Set<String> roleNames;
}
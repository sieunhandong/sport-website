package com.thanh_phoi_co.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class LoginRequest implements Serializable {
    @NotBlank(message = "Username must be not null")
    @NotNull(message = "Username must be not null")
    @NotEmpty(message = "Username must be not empty")
    private String username;

    @NotBlank(message = "Password must be not null")
    @NotNull(message = "Password must be not null")
    @NotEmpty(message = "Password must be not empty")
    private String password;
}

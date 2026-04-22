package com.thanh_phoi_co.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ChangePasswordRequest implements Serializable {

    @NotNull(message = "secretKey must be not null")
    private String secretKey;

    @NotBlank(message = "Password must be not null")
    private String password;

    @NotBlank(message = "Password must be not null")
    private String confirmPassword;
}

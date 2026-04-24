package com.thanh_phoi_co.dto.request;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPasswordRequest {
    @Email
    private String email;
}

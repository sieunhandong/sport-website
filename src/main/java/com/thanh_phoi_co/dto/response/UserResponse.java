package com.thanh_phoi_co.dto.response;

import com.thanh_phoi_co.enums.Gender;
import com.thanh_phoi_co.enums.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String firstName;
    String lastName;
    String email;
    String phone;
    LocalDateTime dateOfBirth;
    Gender gender;
    UserStatus status;
    String username;
    Set<RoleResponse> roles;
}

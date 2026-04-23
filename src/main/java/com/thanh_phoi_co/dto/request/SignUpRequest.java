package com.thanh_phoi_co.dto.request;

import com.thanh_phoi_co.dto.validator.EnumValue;
import com.thanh_phoi_co.dto.validator.PhoneNumber;
import com.thanh_phoi_co.dto.validator.ValidLocalDateTime;
import com.thanh_phoi_co.enums.Gender;
import com.thanh_phoi_co.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest implements Serializable {

    @Column(name = "first_name")
    @NotNull(message = "First Name must be not null")
    @NotBlank(message = "First Name is not blank")
    @NotEmpty(message = "First Name must be not empty")
    private String firstName;

    @NotNull(message = "Last Name must be not null")
    @NotBlank(message = "Last Name is not blank")
    @NotEmpty(message = "Last Name must be not empty")
    private String lastName;

    @Email(message = "Email wrong format")
    private String email;

    @PhoneNumber
    private String phone;
// dùng như này th không cần custom validate
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

//    @ValidLocalDateTime(pattern = "yyyy-MM-dd HH:mm:ss", future = true, message = "Date of birth must be in the future")
//    private LocalDateTime dateOfBirth;

    @EnumValue(enumClass = Gender.class, message = "Gender must be one of the following: {enum}")
    private Gender gender;

    @EnumValue(enumClass = UserStatus.class, message = "Status must be one of the following: {enum}")
    private UserStatus status;

    @NotNull(message = "Username must be not null")
    @NotBlank(message = "Username is not blank")
    @NotEmpty(message = "Username must be not empty")
    private String username;

    @NotNull(message = "Password must be not null")
    @NotBlank(message = "Password is not blank")
    @NotEmpty(message = "Password must be not empty")
    private String password;

    @NotNull(message = "Password confirm must be not null")
    @NotBlank(message = "Password confirm is not blank")
    @NotEmpty(message = "Password confirm must be not empty")
    private String confirmPassword;

//    private List<String> roles;

}

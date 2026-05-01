package com.thanh_phoi_co.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DressVariantRequest {

    @NotBlank(message = "Size không được để trống")
    private String size;

    @NotBlank(message = "Màu sắc không được để trống")
    private String color;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải >= 1")
    private Integer quantityAvailable;
}
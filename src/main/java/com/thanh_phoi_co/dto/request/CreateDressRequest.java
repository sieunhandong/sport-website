package com.thanh_phoi_co.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDressRequest {

    @NotBlank(message = "Tên váy không được để trống")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    private String material;

    @NotNull(message = "Giá thuê không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá thuê phải lớn hơn 0")
    private BigDecimal rentalPrice;

    @NotNull(message = "Tiền cọc không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Tiền cọc phải lớn hơn 0")
    private BigDecimal depositAmount;

    @NotBlank(message = "Category ID không được để trống")
    private String categoryId;

    @NotEmpty(message = "Phải có ít nhất một biến thể")
    private List<DressVariantRequest> variants;
}

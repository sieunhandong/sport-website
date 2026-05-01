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
public class UpdateDressRequest {

    private String name;
    private String description;
    private String material;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal rentalPrice;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal depositAmount;

    private List<DressVariantRequest> variants; // Có thể cập nhật variants
}

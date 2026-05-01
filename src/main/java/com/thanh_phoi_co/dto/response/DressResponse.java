package com.thanh_phoi_co.dto.response;

import com.thanh_phoi_co.enums.DressStatus;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DressResponse {

    private String id;
    private String name;
    private String description;
    private String material;
    private BigDecimal rentalPrice;
    private BigDecimal depositAmount;
    private DressStatus status;
    private String categoryId;
    private String categoryName;
    private List<DressImageResponse> images;
    private List<DressVariantResponse> variants;
    private Double averageRating;
    private Integer reviewCount;
}

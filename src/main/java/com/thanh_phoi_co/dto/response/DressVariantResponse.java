package com.thanh_phoi_co.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DressVariantResponse {

    private String id;
    private String size;
    private String color;
    private Integer quantityAvailable;
    private Integer quantityRented;
}

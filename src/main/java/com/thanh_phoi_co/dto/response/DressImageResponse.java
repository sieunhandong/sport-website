package com.thanh_phoi_co.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DressImageResponse {

    private String id;
    private String imageUrl;
    private String altText;
    private Boolean isPrimary;
}

package com.thanh_phoi_co.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DressCategoryResponse {

    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private Long dressCount;
}

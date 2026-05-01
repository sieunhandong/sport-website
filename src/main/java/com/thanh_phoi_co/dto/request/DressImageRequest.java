package com.thanh_phoi_co.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DressImageRequest {

    @NotBlank(message = "Image URL không được để trống")
    private String imageUrl;

    private String altText;

    private Boolean isPrimary = false;
}

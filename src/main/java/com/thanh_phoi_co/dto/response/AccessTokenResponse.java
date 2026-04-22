package com.thanh_phoi_co.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccessTokenResponse {
    private String accessToken;
    private String userId;
}

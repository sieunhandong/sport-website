package com.thanh_phoi_co.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {
    @JsonProperty("ACTIVE")
    ACTIVE,
    @JsonProperty("INACTIVE")
    INACTIVE,
    @JsonProperty("NONE")
    NONE
}

package com.thanh_phoi_co.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Roles {

    @JsonProperty("ADMIN")
    ADMIN,
    @JsonProperty("USER")
    USER,
    @JsonProperty("OTHER")
    OTHER
}

package com.ssafy.dokcho2.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ROLE_ADMIN, ROLE_USER;

    @JsonCreator
    public static Role from(String value) {
        return Role.valueOf(value.toUpperCase());
    }
}
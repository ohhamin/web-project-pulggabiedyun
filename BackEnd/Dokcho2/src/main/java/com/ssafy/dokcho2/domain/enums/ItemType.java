package com.ssafy.dokcho2.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ItemType {
    STORY_ITEM, RELIC_ITEM, USE_ITEM;

    @JsonCreator
    public static ItemType from(String value) {
        return ItemType.valueOf(value.toUpperCase());
    }
}

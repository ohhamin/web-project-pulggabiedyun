package com.ssafy.dokcho2.dto.exception.item;

import com.ssafy.dokcho2.dto.exception.CustomException;
import com.ssafy.dokcho2.dto.exception.ErrorCode;

public class ItemNotUsable extends CustomException {
    public ItemNotUsable() {
        super(ErrorCode.ITEM_NOT_USABLE);
    }
}

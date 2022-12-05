package com.ssafy.dokcho2.dto.exception.item;

import com.ssafy.dokcho2.dto.exception.CustomException;
import com.ssafy.dokcho2.dto.exception.ErrorCode;

public class ItemNotFoundException extends CustomException {
    public ItemNotFoundException() {
        super(ErrorCode.ITEM_NOT_EXIST);
    }
}

package com.ssafy.dokcho2.dto.exception.item;

import com.ssafy.dokcho2.dto.exception.CustomException;
import com.ssafy.dokcho2.dto.exception.ErrorCode;

public class UserItemNotExistException extends CustomException {
    public UserItemNotExistException() {
        super(ErrorCode.ITEM_NOT_IN_INVENTORY);
    }
}

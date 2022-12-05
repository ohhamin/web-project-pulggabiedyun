package com.ssafy.dokcho2.dto.exception.monster;

import com.ssafy.dokcho2.dto.exception.CustomException;
import com.ssafy.dokcho2.dto.exception.ErrorCode;

public class MonsterAlreadyExist extends CustomException {
    public MonsterAlreadyExist() {
        super(ErrorCode.ITEM_NOT_EXIST);
    }
}

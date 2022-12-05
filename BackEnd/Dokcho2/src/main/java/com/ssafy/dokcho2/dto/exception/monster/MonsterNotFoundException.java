package com.ssafy.dokcho2.dto.exception.monster;

import com.ssafy.dokcho2.dto.exception.CustomException;
import com.ssafy.dokcho2.dto.exception.ErrorCode;

public class MonsterNotFoundException extends CustomException {
    public MonsterNotFoundException() {
        super(ErrorCode.MONSTER_NOT_FOUND);
    }
}

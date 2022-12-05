package com.ssafy.dokcho2.dto.exception.mission;

import com.ssafy.dokcho2.dto.exception.CustomException;
import com.ssafy.dokcho2.dto.exception.ErrorCode;

public class MissionNotFoundException extends CustomException {

    public MissionNotFoundException(){
        super(ErrorCode.MISSION_NOT_FOUND);
    }
}

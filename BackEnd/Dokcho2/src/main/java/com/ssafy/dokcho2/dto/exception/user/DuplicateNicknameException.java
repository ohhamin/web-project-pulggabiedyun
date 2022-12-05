package com.ssafy.dokcho2.dto.exception.user;


import com.ssafy.dokcho2.dto.exception.CustomException;
import com.ssafy.dokcho2.dto.exception.ErrorCode;

public class DuplicateNicknameException extends CustomException {
    public DuplicateNicknameException(){
        super(ErrorCode.DUPLICATE_NICKNAME);
    }
}

package com.ssafy.dokcho2.dto.exception.user;


import com.ssafy.dokcho2.dto.exception.CustomException;
import com.ssafy.dokcho2.dto.exception.ErrorCode;

public class DuplicateUsernameException extends CustomException {
    public DuplicateUsernameException(){
        super(ErrorCode.DUPLICATE_USERNAME);
    }
}

package com.ssafy.dokcho2.dto.exception.user;


import com.ssafy.dokcho2.dto.exception.CustomException;
import com.ssafy.dokcho2.dto.exception.ErrorCode;

public class DuplicateEmailException extends CustomException {
    public DuplicateEmailException(){
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}

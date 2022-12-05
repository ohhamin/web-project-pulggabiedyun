package com.ssafy.dokcho2.dto.exception.common;

import com.ssafy.dokcho2.dto.exception.CustomException;
import com.ssafy.dokcho2.dto.exception.ErrorCode;
import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class InvalidParameterException extends CustomException {
    private final Errors errors;

    public InvalidParameterException(Errors errors){
        super(ErrorCode.INVALID_PARAMETER);
        this.errors = errors;
    }
}

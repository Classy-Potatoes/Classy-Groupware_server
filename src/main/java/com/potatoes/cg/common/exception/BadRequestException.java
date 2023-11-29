package com.potatoes.cg.common.exception;

import com.potatoes.cg.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class BadRequestException extends CustomException {
    public BadRequestException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}

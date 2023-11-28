package com.potatoes.cg.calendar.common.exception;

import com.potatoes.cg.calendar.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class NotFoundException extends CustomException{

    public NotFoundException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}

package com.potatoes.cg.common.exception;


import com.potatoes.cg.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class ConflictException extends CustomException {


    public ConflictException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

}

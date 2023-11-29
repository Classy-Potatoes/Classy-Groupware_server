package com.potatoes.cg.common.exception;

import com.potatoes.cg.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class NotFoundException extends CustomException{

    public NotFoundException( final ExceptionCode exceptionCode ) {
        super( exceptionCode );
    }
}

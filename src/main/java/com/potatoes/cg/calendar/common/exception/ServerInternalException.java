package com.potatoes.cg.calendar.common.exception;

import com.potatoes.cg.calendar.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class ServerInternalException extends CustomException {

    public ServerInternalException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}

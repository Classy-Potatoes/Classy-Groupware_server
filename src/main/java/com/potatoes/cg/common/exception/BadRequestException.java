package com.potatoes.cg.common.exception;

import com.potatoes.cg.common.exception.type.ExceptionCode;
import lombok.Getter;

/* 그냥 Exception을 상속 받는것보다 RuntimeException 으로 상속 받았을 경우
 * catch를 굳이 할 필요가 없어진다. */
@Getter
public class BadRequestException extends CustomException {

    // CustomException 으로 코드 정리
//    private final int code;
//    private final String message;

    public BadRequestException(final ExceptionCode exceptionCode) {

//        // 넘어오는 code와 message를 호출해서 등록
//        this.code = exceptionCode.getCode();
//        this.message = exceptionCode.getMessage();
        super(exceptionCode);
    }
}

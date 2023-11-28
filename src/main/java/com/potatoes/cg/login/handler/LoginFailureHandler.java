package com.potatoes.cg.login.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.potatoes.cg.common.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.potatoes.cg.common.exception.type.ExceptionCode.FAIL_LOGIN;


/* 로그인 실패 처리 핸들러 */
@RequiredArgsConstructor
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {


    private final ObjectMapper objectMapper;


    // 실패했을때 호출될 메소드 오버라이딩 선언
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        // body 내용을 출력스트림 사용해서 출력
        // java obj -> JSON String 으로 변환
        response.getWriter().write( objectMapper.writeValueAsString( new ExceptionResponse( FAIL_LOGIN ) ) );

    }


}

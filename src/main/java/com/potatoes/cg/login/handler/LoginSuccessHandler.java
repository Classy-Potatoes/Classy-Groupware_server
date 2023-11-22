package com.potatoes.cg.login.handler;

import com.ohgiraffers.comprehensive.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/* 로그인 성공 핸들러 */
@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private final JwtService jwtService;


    // 성공시 호출될 메소드 오버라이딩
    // 로그인에 성공했을 경우 Authentication (인증객체) 를 받아온다.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        /* 로그인 성공 후 저장 된 인증 객체에서 정보를 꺼낸다. */
        Map<String, String> memberInfo = getMemberInfo(authentication);
        log.info("로그인 성공 후 인증 객체에서 꺼낸 정보 : {}", memberInfo);

        /* access token과 refresh token 생성 */
        String accessToken = jwtService.createAccessToken(memberInfo);
        String refreshToken = jwtService.createRefreshToken();

        log.info("발급 된 accessToken : {}", accessToken );
        log.info("발급 된 refreshToken : {}", refreshToken );

        /* 응답 헤더에 발급 된 토근을 담는다. */
        response.setHeader( "Access-Token", accessToken );
        response.setHeader( "Refresh-Token", refreshToken );
        response.setStatus( HttpServletResponse.SC_OK );


        /* 발급한 refresh token을 DB에 저장해 둔다. */
        // 누군지 알아야하니 조회할 id값과 리프레쉬 토큰을 같이 보낸다.
        jwtService.updateRefreshToken( memberInfo.get("memberId"), refreshToken );

    }


    private Map<String, String> getMemberInfo( Authentication authentication ) {

        // authentication 안에는 UserDetails 가 들어있다.

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String memberRole = userDetails.getAuthorities()
                .stream().map(auth -> auth.getAuthority().toString())
                .collect(Collectors.joining());

        // 인증객체로부터 id,role 정보를 뽑아낸다.
        return Map.of(
                    "memberId", userDetails.getUsername(),
                    "memberRole", memberRole
                );

    }



}

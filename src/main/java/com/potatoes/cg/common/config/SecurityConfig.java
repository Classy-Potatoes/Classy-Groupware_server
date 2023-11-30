package com.potatoes.cg.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potatoes.cg.jwt.filter.JwtAuthenticationFilter;
import com.potatoes.cg.jwt.handler.JwtAccessDeniedHandler;
import com.potatoes.cg.jwt.handler.JwtAuthenticationEntryPoint;
import com.potatoes.cg.jwt.service.JwtService;
import com.potatoes.cg.login.filter.CustomUsernamePasswordAuthenticationFilter;
import com.potatoes.cg.login.handler.LoginFailureHandler;
import com.potatoes.cg.login.handler.LoginSuccessHandler;
import com.potatoes.cg.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final ObjectMapper objectMapper;

    private final LoginService loginService;

    private final JwtService jwtService;


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                // CSRF 설정 비활성화
                .csrf()
                .disable()
                // API 서버는 session을 사용하지 않으므로 STATELESS 설정
                // 기본이 session이 들어가있으나 상태값 없음으로 설정
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 요청에 대한 권한 체크  --------
                .authorizeHttpRequests()
                // 클라이언트가 외부 도메인을 요청하는 경우 웹 브라우저에서 자체적으로 사전 요청(preflight)이 일어남
                // 이 때 OPTIONS 메서드를 서버에 사전 요청을 보내 권한을 확인함
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 이미지 허가
                .antMatchers(HttpMethod.GET, "/imgs/**").permitAll()


                // 이런 경로의 요청은 다 허락하겠다. 비로그인 상태에서도 볼수 있다.
                .antMatchers(HttpMethod.GET, "/member/login/**").permitAll()
//                .antMatchers("/**").permitAll()       // 임시 작동


                .antMatchers("/cg-api/v1/member/regist").permitAll()       // 회원가입도 비로그인 상태에서 가능
                // 이런 패턴들은 관리자 권한이 있는 사람만 가능하다(인증, 인가 둘다 가능해야 수행할수 있다.)
                .antMatchers("/cg-api/v1/ad/**").hasRole("ADMIN")
                // 여기에 선언된 요청 외에는 모든것은 인증되어야만 한다.
                .anyRequest().authenticated()
                .and()
                // 로그인 필터 설정
                // (커스텀한 customUsernamePasswordAuthenticationFilter 필터를 원래 있던 UsernamePasswordAuthenticationFilter 필터 앞에 끼워 넣겠다.)
                .addFilterBefore( customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class )
                // JWT Token 인증 필터 설정 (로그인 필터 앞에 설정)
                .addFilterBefore( jwtAuthenticationFilter(), CustomUsernamePasswordAuthenticationFilter.class )
                // exception handling 설정
                .exceptionHandling()
                // 인증실패
                .authenticationEntryPoint( jwtAuthenticationEntryPoint() )
                // 인가실패
                .accessDeniedHandler( jwtAccessDeniedHandler() )
                .and()
                // 필터 순서
                // jwtAuthenticationFilter -> CustomUsernamePasswordAuthenticationFilter
                //      -> UsernamePasswordAuthenticationFilter
                // ------------------------

                // 교차 출처 자원 공유 설정 (밑에 설정해둔 corsConfigurationSource 로 처리됨)
                .cors()
                .and()
                .build();
    }


    /* CORS(Cross Origin Resource Sharing) : 교차 출처 자원 공유
    * 보안상 웹 브라우저는 다른 도메인에서 서버의 자원을 요청하는 경우 맞아 놓았음.
    * 기본적으로 서버에서 클라이언트를 대상으로 리소스 허용 여부를 결정함.
    *  */

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 로컬 React에서 오는 요청은 허용한다. Origins (ip, port)
        // 이런식으로 추가해서 사용할수 있도록 list로 만들었다.
//        corsConfiguration.setAllowedOrigins(Arrays.asList("https://localhost:3000", "", ""));
        corsConfiguration.setAllowedOrigins( Arrays.asList("http://localhost:3000") );
        corsConfiguration.setAllowedMethods( Arrays.asList("GET", "PUT", "POST", "DELETE") );
        corsConfiguration.setAllowedHeaders( Arrays.asList(
                "Access-Control-Allow-Origin", "Access-Control-Allow-Headers",
                "Content-Type", "Authorization", "X-Requested-With", "Access-Token", "Refresh-Token") );
        // 응답에 대한 설정 res
        corsConfiguration.setExposedHeaders( Arrays.asList("access-Token", "refresh-Token") );

        // 모든 요청 url 패턴에 대해 위의 설정을 적용한다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }


    // 패스워드 인코더 (암호화 패스워드)
    @Bean
    public PasswordEncoder passwordEncoder() {

        // 여러가지 중에 BCryptPasswordEncoder 사용. (특징은 랜덤 sorting 기법을 사용)
        return new BCryptPasswordEncoder();
    }


    /* 인증 매니저 빈 등록 => 로그인 시 사용할 password encode 설정,
    * 로그인 시 유저 조회하는 메소드를 가진 Service 클래스 설정
    * ----------------------------------------------
    * 밑에 필터가 사용할 인증 매니저 (아이디와 비밀번호 체킹을 여기서 한다)
    * */
    @Bean
    public AuthenticationManager authenticationManager() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder( passwordEncoder() );       // 비밀번호 맞는지 체크 (인코더 사용해서)
        provider.setUserDetailsService( loginService );         // DB에서 아이디가 맞는지 조회해오는 서비스를 하나만들어서

        return new ProviderManager( provider );

    }


    /* 로그인 실패 핸들러 빈 등록 */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler( objectMapper );
    }


    /* 로그인 성공 핸들러 빈 등록
    * 토큰 생성을 위해서 jwtService를 넘겨줘야한다. */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler( jwtService );
    }


    /* 로그인 필터 빈 등록 */
    /* CustomUsernamePasswordAuthenticationFilter 만든것을 빈 등록
    * 위쪽의 authenticationManager로 이동 */
    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() {

        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter
                = new CustomUsernamePasswordAuthenticationFilter( objectMapper );

        /* 사용할 인증 매니저 설정 (상단 매니저 적용) */
        customUsernamePasswordAuthenticationFilter.setAuthenticationManager( authenticationManager() );

        /* 로그인 실패 핸들링 */
        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler( loginFailureHandler() );

        /* 로그인 성공 핸들링
        * 성공시 성공핸들링에서 액세스토큰, 리프레쉬 토큰 생성, DB에 저장 동작함.
        * */
        customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler( loginSuccessHandler() );

        return customUsernamePasswordAuthenticationFilter;

    }


    /* JWT 인증 필터 */
    // 만든 필터를 빈으로 등록
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter( jwtService );
    }


    /* 인증 실패 핸들러 */
    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint () {

        return new JwtAuthenticationEntryPoint( objectMapper );
    }

    /* 인가 실패 핸들러 */
    @Bean
    public JwtAccessDeniedHandler jwtAccessDeniedHandler() {

        return new JwtAccessDeniedHandler( objectMapper );
    }


}

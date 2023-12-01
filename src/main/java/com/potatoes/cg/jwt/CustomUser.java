package com.potatoes.cg.jwt;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUser extends User {

    /* 미리 memberCode 정보 삽입 */
    private final Long memberCode;

    /* 미리 infoCode(사번) 정보 삽입 */
    private final Long infoCode;

    // 부서, 직급코드는 infoCode로 각 로직에서 조회해서 사용바랍니다.


    public CustomUser( Long memberCode, Long infoCode,  UserDetails userDetails ) {

        super(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        this.memberCode = memberCode;
        this.infoCode = infoCode;
    }

    public static CustomUser of( Long memberCode, Long infoCode, UserDetails userDetails ) {

        return new CustomUser(
                memberCode,
                infoCode,
                userDetails
        );

    }
}

package com.potatoes.cg.jwt;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUser extends User {

    /* 인증할때 미리 memberCode를 넣어두려고 */
    private final Long memberCode;

    public CustomUser( Long memberCode, UserDetails userDetails ) {

        super(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        this.memberCode = memberCode;
    }

    public static CustomUser of( Long memberCode, UserDetails userDetails ) {

        return new CustomUser(
                memberCode,
                userDetails
        );

    }
}

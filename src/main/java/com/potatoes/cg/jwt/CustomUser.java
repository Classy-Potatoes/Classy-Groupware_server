package com.potatoes.cg.jwt;

import com.potatoes.cg.member.domain.MemberInfo;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUser extends User {

    /* 인증할때 미리 memberCode를 넣어두려고 */
    private final Long memberCode;

    /* 인증할때 미리 infoCode(사번)도 같이 넣어둔다. */
    private final Long infoCode;

    public CustomUser( Long memberCode, Long infoCode, UserDetails userDetails ) {

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

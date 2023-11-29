package com.potatoes.cg.member.dto.response;

import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ProfileResponse {

    private final String memberId;
//    private final String memberName;
//    private final String memberEmail;

    public static ProfileResponse from( final Member member ) {

        return new ProfileResponse(
                member.getMemberId()
//                member.getMemberId(),
//                member.getMemberName(),
//                member.getMemberEmail()
        );

    }

}

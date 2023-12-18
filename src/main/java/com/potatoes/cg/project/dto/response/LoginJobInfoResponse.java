package com.potatoes.cg.project.dto.response;

import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class LoginJobInfoResponse {

    private final Long infoCode;
    private final Long jobCode;

    public static LoginJobInfoResponse from(Member member) {
        return new LoginJobInfoResponse(
                member.getMemberInfo().getInfoCode(),
                member.getMemberInfo().getJob().getJobCode()
        );
    }
}

package com.potatoes.cg.approval.dto.response;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class LoginUserInfoResponse {

    private final String jobName;
    private final String deptName;
    private final String infoName;


    public static LoginUserInfoResponse from(Member member) {
        return new LoginUserInfoResponse(
                member.getMemberInfo().getJob().getJobName(),
                member.getMemberInfo().getDept().getDeptName(),
                member.getMemberInfo().getInfoName()
        );
    }
}

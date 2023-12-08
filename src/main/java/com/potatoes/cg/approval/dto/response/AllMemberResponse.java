package com.potatoes.cg.approval.dto.response;

import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class AllMemberResponse {

    private final Long memberCode;
    private final String jobName;
    private final String deptName;
    private final String infoName;



    public static AllMemberResponse from(Member member) {
        return new AllMemberResponse(
                member.getMemberCode(),
                member.getMemberInfo().getJob().getJobName(),
                member.getMemberInfo().getDept().getDeptName(),
                member.getMemberInfo().getInfoName()
        );
    }


}

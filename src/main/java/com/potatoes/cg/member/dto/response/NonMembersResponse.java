package com.potatoes.cg.member.dto.response;

import com.potatoes.cg.member.domain.History;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.type.MemberInfoStatus;
import com.potatoes.cg.member.domain.type.MemberStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class NonMembersResponse {

    private final String infoName;
    private final Long infoCode;
    private final String jobName;
    private final String deptName;
    private final MemberInfoStatus infoStatus;
    private final String infoPhone;
    private final String infoEmail;


    public static NonMembersResponse from(final MemberInfo memberInfo ) {

        return new NonMembersResponse(
                memberInfo.getInfoName(),
                memberInfo.getInfoCode(),
                memberInfo.getJob().getJobName(),
                memberInfo.getDept().getDeptName(),
                memberInfo.getInfoStatus(),
                memberInfo.getInfoPhone(),
                memberInfo.getInfoEmail()
        );

    }

}

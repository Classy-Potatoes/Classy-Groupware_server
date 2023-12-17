package com.potatoes.cg.member.dto.response;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.type.MemberInfoStatus;
import com.potatoes.cg.member.domain.type.MemberStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class AdminMembersResponse {

    private final String filePath;
    private final String infoName;
    private final Long infoCode;
    private final String jobName;
    private final String deptName;
    private final String infoPhone;
    private final String infoEmail;
    private final Long memberCode;
    private final MemberStatus memberStatus;


    public static AdminMembersResponse from(final Member member ) {

        return new AdminMembersResponse(
                member.getProfileImage().get(0).getFilePathName(),
                member.getMemberInfo().getInfoName(),
                member.getMemberInfo().getInfoCode(),
                member.getMemberInfo().getJob().getJobName(),
                member.getMemberInfo().getDept().getDeptName(),
                member.getMemberInfo().getInfoPhone(),
                member.getMemberInfo().getInfoEmail(),
                member.getMemberCode(),
                member.getMemberStatus()
        );

    }

}

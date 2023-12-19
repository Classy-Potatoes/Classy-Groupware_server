package com.potatoes.cg.note.dto.response;

import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class NoteMemberListResponse {

    private final String infoName;
    private final String jobName;
    private final String deptName;
    private final Long memberCode;


    public static NoteMemberListResponse from(final Member member) {

        return new NoteMemberListResponse(
                member.getMemberInfo().getInfoName(),
                member.getMemberInfo().getJob().getJobName(),
                member.getMemberInfo().getDept().getDeptName(),
                member.getMemberCode()
        );

    }

}
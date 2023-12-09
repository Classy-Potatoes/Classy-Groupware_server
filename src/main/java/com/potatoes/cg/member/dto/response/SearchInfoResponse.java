package com.potatoes.cg.member.dto.response;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class SearchInfoResponse {

    private final String infoName;
    private final String deptName;
    private final String jobName;

    public static SearchInfoResponse from( final MemberInfo info ) {

        return new SearchInfoResponse(
                info.getInfoName(),
                info.getDept().getDeptName(),
                info.getJob().getJobName()
        );

    }

}

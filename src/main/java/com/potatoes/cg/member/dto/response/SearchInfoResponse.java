package com.potatoes.cg.member.dto.response;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class SearchInfoResponse {

    private final Long infoCode;
    private final Long deptCode;
    private final Long jobCode;

    public static SearchInfoResponse from(final MemberInfo info ) {

        return new SearchInfoResponse(
                info.getInfoCode(),
                info.getDept().getDeptCode(),
                info.getJob().getJobCode()
        );

    }

}

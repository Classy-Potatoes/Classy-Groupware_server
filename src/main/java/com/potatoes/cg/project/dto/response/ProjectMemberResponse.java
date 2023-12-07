package com.potatoes.cg.project.dto.response;

import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ProjectMemberResponse {

    private final Long infoCode;

    private final String memberName;



    public static ProjectMemberResponse from(Long infoCode, String infoName) {
        return new ProjectMemberResponse(
                infoCode,
                infoName
        );
    }

    public static ProjectMemberResponse fromMember(final Member member) {

            return new ProjectMemberResponse(
                    member.getMemberInfo().getInfoCode(),
                    member.getMemberInfo().getInfoName()
            );

    }
}


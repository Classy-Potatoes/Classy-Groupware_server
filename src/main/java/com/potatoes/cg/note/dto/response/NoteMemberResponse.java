package com.potatoes.cg.note.dto.response;

import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NoteMemberResponse {

    private final Long infoCode;

    private final String memberName;

    public static NoteMemberResponse from(Long infoCode, String infoName) {
        return new NoteMemberResponse(
                infoCode,
                infoName
        );
    }

    public static NoteMemberResponse fromMember(final Member member) {

        return new NoteMemberResponse(
                member.getMemberInfo().getInfoCode(),
                member.getMemberInfo().getInfoName()
        );

    }

}
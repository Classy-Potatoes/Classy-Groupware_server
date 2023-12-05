package com.potatoes.cg.project.dto.response;

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
}


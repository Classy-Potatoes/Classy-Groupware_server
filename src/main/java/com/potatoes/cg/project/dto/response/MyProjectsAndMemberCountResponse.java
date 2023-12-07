package com.potatoes.cg.project.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyProjectsAndMemberCountResponse {

    private ProjectsResponse project;
    private long participantCount;

    public MyProjectsAndMemberCountResponse(ProjectsResponse project, long participantCount) {
        this.project = project;
        this.participantCount = participantCount;
    }
}

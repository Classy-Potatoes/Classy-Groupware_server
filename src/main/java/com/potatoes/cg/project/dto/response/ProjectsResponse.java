package com.potatoes.cg.project.dto.response;

import com.potatoes.cg.project.domain.Project;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor
public class ProjectsResponse {

    private final Long projectCode;
    private final String projectTitle;
    private final String deptName;
    private Long participantCount;

    public ProjectsResponse(Long projectCode, String projectTitle, String deptName, Long participantCount) {
        this.projectCode = projectCode;
        this.projectTitle = projectTitle;
        this.deptName = deptName;
        this.participantCount = participantCount;
    }

    public static ProjectsResponse from(final Project project, Long participantCount) {
        return new ProjectsResponse(
                project.getProjectCode(),
                project.getProjectTitle(),
                project.getDept().getDeptName(),
                participantCount

        );
    }
}



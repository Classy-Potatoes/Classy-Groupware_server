package com.potatoes.cg.project.dto.response;

import com.potatoes.cg.project.domain.Project;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ProjectsResponse {

    private final Long projectCode;
    private final String projectTitle;
    private final String deptName;

    public static ProjectsResponse from(final Project project) {
        return new ProjectsResponse(
                project.getProjectCode(),
                project.getProjectTitle(),
                project.getDept().getDeptName()
        );
    }
}

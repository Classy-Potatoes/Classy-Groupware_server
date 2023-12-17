package com.potatoes.cg.project.dto.request;

import com.potatoes.cg.member.domain.MemberInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class ProjectTaskCreateRequest {

    @NotBlank
    private final String taskTitle;

    @NotBlank
    private final String taskBody;

    private String taskStatus;

    private final Date taskStartDate;

    private final Date taskEndDate;

    private final String taskPriority;

    @Min(value = 1)
    private final Long projectCode;

    private final List<MemberInfo> projectManagers;
}

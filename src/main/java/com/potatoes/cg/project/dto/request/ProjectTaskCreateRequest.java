package com.potatoes.cg.project.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@RequiredArgsConstructor
@Getter
public class ProjectTaskCreateRequest {

    @NotBlank
    private final String taskTitle;

    @NotBlank
    private final String taskBody;

    private final Date taskStartDate;

    private final Date taskEndDate;

    private final String taskPriority;

    @Min(value = 1)
    private final Long projectCode;
}

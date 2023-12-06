package com.potatoes.cg.project.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@RequiredArgsConstructor
@Getter
@NoArgsConstructor(force = true)
public class TaskUpdateRequest {

    @NotBlank
    private final String taskTitle;

    @NotBlank
    private final String taskBody;

    private final Date taskStartDate;

    private final Date taskEndDate;

    private final String taskPriority;
}

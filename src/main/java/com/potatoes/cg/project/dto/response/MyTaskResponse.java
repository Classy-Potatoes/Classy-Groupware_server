package com.potatoes.cg.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.project.domain.ProjectTask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.config.Task;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MyTaskResponse {

    private final Long taskCode;
    private final String taskTitle;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime taskRequestDate;

    public static MyTaskResponse from(ProjectTask projectTask) {
        return new MyTaskResponse(
                projectTask.getTaskCode(),
                projectTask.getTaskTitle(),
                projectTask.getTaskRequestDate()
        );
    }
}


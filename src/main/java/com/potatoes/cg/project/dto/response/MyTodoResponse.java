package com.potatoes.cg.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.projectTodolist.domain.ProjectTodolist;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@RequiredArgsConstructor
public class MyTodoResponse {

    private final Long todoListCode;
    private final Long todoCode;
    private final String todoBody;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate todoEndDate;

    public static MyTodoResponse from(ProjectTodolist projectTodolist) {
        return new MyTodoResponse(
                projectTodolist.getTodoListCode(),
                projectTodolist.getTodoCode(),
                projectTodolist.getTodoBody(),
                projectTodolist.getTodoEndDate()
        );
    }
}

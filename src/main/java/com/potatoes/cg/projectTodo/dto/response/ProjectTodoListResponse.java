package com.potatoes.cg.projectTodo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.calendar.domain.type.StatusType;
import com.potatoes.cg.project.domain.ProjectReply;
import com.potatoes.cg.projectTodolist.domain.ProjectTodolist;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class ProjectTodoListResponse {

    private final Long todoListCode;
    private final String todoListBody;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate todoListEndDate;

    private final String managerName;
    private final StatusType todoStatus;


    public static ProjectTodoListResponse from(ProjectTodolist projectTodolist) {

        return new ProjectTodoListResponse(
                projectTodolist.getTodoListCode(),
                projectTodolist.getTodoBody(),
                projectTodolist.getTodoEndDate(),
                projectTodolist.getProjectManager().getMember().getMemberInfo().getInfoName(),
                projectTodolist.getTofoStatus()
        );
    }

}

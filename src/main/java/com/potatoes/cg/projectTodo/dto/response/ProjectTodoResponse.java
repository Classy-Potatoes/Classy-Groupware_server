package com.potatoes.cg.projectTodo.dto.response;

import com.potatoes.cg.projectTodo.domain.ProjectTodo;
import com.potatoes.cg.projectTodolist.domain.ProjectTodolist;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ProjectTodoResponse {

    private final Long todoCode;
    private final String todoTitle;
    private final String memberName;
    private final List<ProjectTodoListResponse> todoList;
    private final String memberId;

    public static ProjectTodoResponse from(ProjectTodo projectTodo, List<ProjectTodoListResponse> projectTodolists) {
        return new ProjectTodoResponse(
                projectTodo.getTodoCode(),
                projectTodo.getTodoTitle(),
                projectTodo.getMember().getMemberInfo().getInfoName(),
                projectTodolists,
                projectTodo.getMember().getMemberId()

        );
    }
}

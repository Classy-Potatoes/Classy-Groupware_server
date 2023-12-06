package com.potatoes.cg.projectTodo.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.projectTodolist.domain.ProjectTodolist;
import com.potatoes.cg.projectTodolist.dto.request.ProjectTodolistCreateRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class ProjectTodoCreateRequest {

    @NotBlank
    private final String todoTitle;

    private final List<ProjectTodolistCreateRequest> projectTodolistCreateRequestList;
}

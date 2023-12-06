package com.potatoes.cg.projectTodo.dto.request;

import com.potatoes.cg.projectTodolist.dto.request.ProjectTodolistCreateRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class ProjectTodoUpdateRequest {

    @NotBlank
    private final String todoTitle;

    private final List<ProjectTodolistCreateRequest> projectTodolistCreateRequestList;
}

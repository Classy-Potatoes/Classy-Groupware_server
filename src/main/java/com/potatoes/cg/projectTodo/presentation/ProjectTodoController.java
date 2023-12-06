package com.potatoes.cg.projectTodo.presentation;

import com.potatoes.cg.projectSchedule.service.ProjectScheduleService;
import com.potatoes.cg.projectTodo.dto.request.ProjectTodoCreateRequest;
import com.potatoes.cg.projectTodo.dto.request.ProjectTodoUpdateRequest;
import com.potatoes.cg.projectTodo.service.ProjectTodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/cg-api/v1/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectTodoController {

    private final ProjectTodoService projectTodoService;
    private final ProjectScheduleService projectScheduleService;

    /* 할일글 등록 */
    @PostMapping("/{projectCode}/todo")
    public ResponseEntity<Void> save(@PathVariable @Valid final Long projectCode,
                                     @RequestBody @Valid final ProjectTodoCreateRequest todoRequest) {
        int memberCode = 1;

        projectTodoService.save(projectCode, todoRequest, memberCode);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /* 할일글 수정 */
    @PutMapping("/{projectCode}/todo/{todoCode}")
    public ResponseEntity<Void> update(@PathVariable @Valid final Long projectCode,
                                       @PathVariable @Valid final Long todoCode,
                                       @RequestBody @Valid final ProjectTodoUpdateRequest todoRequest) {

        projectTodoService.update(projectCode, todoCode, todoRequest);

        return ResponseEntity.created(URI.create("/todo-management" + todoCode)).build();
    }

    /* 할일글 삭제 */
    @DeleteMapping("/{projectCode}/todo/{todoCode}")
    public ResponseEntity<Void> delete(@PathVariable final Long projectCode,
                                       @PathVariable final Long todoCode) {

        projectTodoService.delete(projectCode, todoCode);

        return ResponseEntity.noContent().build();
    }
}

package com.potatoes.cg.projectTodo.presentation;

import com.potatoes.cg.calendar.dto.response.ScheduleResponse;
import com.potatoes.cg.common.paging.Pagenation;
import com.potatoes.cg.common.paging.PagingButtonInfo;
import com.potatoes.cg.common.paging.PagingResponse;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.project.dto.response.MyTaskResponse;
import com.potatoes.cg.projectSchedule.dto.request.SchReplyUpdate;
import com.potatoes.cg.projectSchedule.dto.request.ScheduleReplyCreateRequest;
import com.potatoes.cg.projectSchedule.dto.response.ProjectScheduleResponse;
import com.potatoes.cg.projectSchedule.service.ProjectScheduleService;
import com.potatoes.cg.projectTodo.dto.request.ProjectTodoCreateRequest;
import com.potatoes.cg.projectTodo.dto.request.ProjectTodoUpdateRequest;
import com.potatoes.cg.projectTodo.dto.response.ProjectTodoResponse;
import com.potatoes.cg.projectTodo.service.ProjectTodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

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
                                     @RequestBody @Valid final ProjectTodoCreateRequest todoRequest,
                                     @AuthenticationPrincipal final CustomUser customUser) {

        projectTodoService.save(projectCode, todoRequest, customUser);

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

    /* 할일글 조회 */
    @GetMapping("/{projectCode}/todo")
    public ResponseEntity<PagingResponse> getSchedule(
            @RequestParam(defaultValue = "1") final Integer page,
            @PathVariable final Long projectCode,
            @AuthenticationPrincipal final CustomUser customUser
    ) {

        final Page<ProjectTodoResponse> projectTodoResponses = projectTodoService.getTodo(page, projectCode, customUser);

        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(projectTodoResponses);

        final PagingResponse pagingResponse = PagingResponse.of(projectTodoResponses.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /* 할인 완료 */
    @PutMapping("{projectCode}/todo/{todoCode}/{todoListCode}")
    public ResponseEntity<Void> checked(@PathVariable final Long projectCode,
                                        @PathVariable final Long todoCode,
                                        @PathVariable final Long todoListCode,
                                        @AuthenticationPrincipal CustomUser customUser) {
        log.info("abcd : {}", todoListCode);

        projectTodoService.checked(projectCode, todoCode, todoListCode, customUser);
        return ResponseEntity.created(URI.create("/todoList-management" + todoListCode)).build();
    }

    /* 내 할일리스트 조회 */
    @GetMapping("/{projectCode}/myTodoList")
    public ResponseEntity<List<TodoListResponse>> getTodoList(@AuthenticationPrincipal final CustomUser customUser) {
        log.info("cxvsdvsdvs : {}", customUser);
        List<TodoListResponse> todoListResponses = projectTodoService.getTodoList(customUser);;

        return ResponseEntity.status(HttpStatus.OK).body(todoListResponses);
    }

    /* 내 할일리스트 조회 {프로젝트 상관 x}*/
    @GetMapping("/myTodoList")
    public ResponseEntity<List<TodoListResponse>> getAllTodoList(@AuthenticationPrincipal final CustomUser customUser) {
        log.info("cxvsdvsdvs : {}", customUser);
        List<TodoListResponse> todoListResponses = projectTodoService.getAllTodoList(customUser);;

        return ResponseEntity.status(HttpStatus.OK).body(todoListResponses);
    }

    /* 일정 댓글 작성 */
    @PostMapping("/{projectCode}/todo/{todoCode}/reply")
    public ResponseEntity<Void> scheduleReply(
            @RequestBody @Valid final ScheduleReplyCreateRequest replyRequest,
            @PathVariable final Long todoCode,
            @AuthenticationPrincipal final CustomUser customUser
    ) {

        final Long replyCode = projectTodoService.todoReply(replyRequest, todoCode, customUser);

        return ResponseEntity.created(URI.create("/todo-reply/" + replyCode)).build();
    }

    /* 댓글 수정 */
    @PutMapping("/{projectCode}/todo/{todoCode}/reply/{replyCode}")
    public ResponseEntity<Void> postReplyUpdate(@PathVariable final Long replyCode,
                                                @RequestBody @Valid final SchReplyUpdate replyRequest) {

        projectTodoService.replyUpdate(replyCode, replyRequest);

        return ResponseEntity.created(URI.create("/reply-management/" + replyCode)).build();

    }

}

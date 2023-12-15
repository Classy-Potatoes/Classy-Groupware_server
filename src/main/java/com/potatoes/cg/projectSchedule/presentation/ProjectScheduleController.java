package com.potatoes.cg.projectSchedule.presentation;

import com.potatoes.cg.common.paging.Pagenation;
import com.potatoes.cg.common.paging.PagingButtonInfo;
import com.potatoes.cg.common.paging.PagingResponse;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.project.dto.request.ReplyUpdateRequest;
import com.potatoes.cg.projectSchedule.dto.request.ProjectScheduleCreatRequest;
import com.potatoes.cg.projectSchedule.dto.request.SchReplyUpdate;
import com.potatoes.cg.projectSchedule.dto.request.ScheduleReplyCreateRequest;
import com.potatoes.cg.projectSchedule.dto.request.ProjectScheduleUpdateRequest;
import com.potatoes.cg.projectSchedule.dto.response.ProjectScheduleResponse;
import com.potatoes.cg.projectSchedule.service.ProjectScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/cg-api/v1/projects")
@RequiredArgsConstructor
public class ProjectScheduleController {

    private final ProjectScheduleService projectScheduleService;

//    /* 프로젝트 인원 조회(이름) */
//    @GetMapping("/{projectCode}/searchMember")
//    public ResponseEntity<List<ProjectMemberResponse>> getMemberList(@PathVariable final Long projectCode) {
//
//        List<ProjectMemberResponse> projectMemberResponseList = projectScheduleService.getMemberList(projectCode);
//
//        return ResponseEntity.status(HttpStatus.OK).body(projectMemberResponseList);
//    }


    /* 일정글 등록 */
    @PostMapping("/{projectCode}/schedule")
    public ResponseEntity<Void> save(@PathVariable @Valid final Long projectCode,
                                     @RequestBody @Valid final ProjectScheduleCreatRequest scheduleRequest,
                                     @AuthenticationPrincipal final CustomUser customUser) {

        final Long scheduleCode = projectScheduleService.save(projectCode, scheduleRequest, customUser);

        return ResponseEntity.created(URI.create("/schedule-management" + scheduleCode)).build();
    }

    /* 일정글 수정 */
    @PutMapping("/{projectCode}/schedule/{scheduleCode}")
    public ResponseEntity<Void> update(@PathVariable @Valid final Long projectCode,
                                       @PathVariable @Valid final Long scheduleCode,
                                       @RequestBody @Valid final ProjectScheduleUpdateRequest scheduleRequest) {

        projectScheduleService.update(projectCode, scheduleCode, scheduleRequest);

        return ResponseEntity.created(URI.create("/schedule-management" + scheduleCode)).build();
    }

    /* 일정글 삭제 */
    @DeleteMapping("/{projectCode}/schedule/{scheduleCode}")
    public ResponseEntity<Void> delete(@PathVariable final Long projectCode,
                                       @PathVariable final Long scheduleCode) {

        projectScheduleService.delete(projectCode, scheduleCode);

        return ResponseEntity.noContent().build();
    }

    /* 일정글 조회 */
    @GetMapping("/{projectCode}/schedule")
    public ResponseEntity<PagingResponse> getSchedule(
            @RequestParam(defaultValue = "1") final Integer page,
            @PathVariable final Long projectCode,
            @AuthenticationPrincipal final CustomUser customUser
    ) {

        final Page<ProjectScheduleResponse> projectScheduleResponses = projectScheduleService.getSchedule(page, projectCode, customUser);

        // 페이징 정보 구성
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(projectScheduleResponses);

        // 응답 구성
        final PagingResponse pagingResponse = PagingResponse.of(projectScheduleResponses.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /* 일정 댓글 작성 */
    @PostMapping("/{projectCode}/schedule/{scheduleCode}/reply")
    public ResponseEntity<Void> scheduleReply(
            @RequestBody @Valid final ScheduleReplyCreateRequest replyRequest,
            @PathVariable final Long scheduleCode,
            @AuthenticationPrincipal final CustomUser customUser
    ) {

        final Long replyCode = projectScheduleService.scheduleReply(replyRequest, scheduleCode, customUser);

        return ResponseEntity.created(URI.create("/schedule-reply/" + replyCode)).build();
    }

    /* 댓글 수정 */
    @PutMapping("/{projectCode}/schedule/{scheduleCode}/reply/{replyCode}")
    public ResponseEntity<Void> postReplyUpdate(@PathVariable final Long replyCode,
                                                @RequestBody @Valid final SchReplyUpdate replyRequest) {

        projectScheduleService.replyUpdate(replyCode, replyRequest);

        return ResponseEntity.created(URI.create("/reply-management/" + replyCode)).build();

    }

}

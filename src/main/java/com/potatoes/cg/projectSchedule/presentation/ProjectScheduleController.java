package com.potatoes.cg.projectSchedule.presentation;

import com.potatoes.cg.projectSchedule.dto.request.ProjectManagerCreateRequest;
import com.potatoes.cg.projectSchedule.dto.request.ProjectScheduleCreatRequest;
import com.potatoes.cg.projectSchedule.dto.request.ProjectScheduleUpdateRequest;
import com.potatoes.cg.projectSchedule.dto.response.ProjectMemberResponse;
import com.potatoes.cg.projectSchedule.service.ProjectScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cg-api/v1/projects")
@RequiredArgsConstructor
public class ProjectScheduleController {

    private final ProjectScheduleService projectScheduleService;

    /* 프로젝트 인원 조회(이름) */
    @GetMapping("/{projectCode}/searchMember")
    public ResponseEntity<List<ProjectMemberResponse>> getMemberList(@PathVariable final Long projectCode) {

        List<ProjectMemberResponse> projectMemberResponseList = projectScheduleService.getMemberList(projectCode);

        return ResponseEntity.status(HttpStatus.OK).body(projectMemberResponseList);
    }


    /* 일정글 등록 */
    @PostMapping("/{projectCode}/schedule")
    public ResponseEntity<Void> save(@PathVariable @Valid final Long projectCode,
                                     @RequestBody @Valid final ProjectScheduleCreatRequest scheduleRequest) {
        int memberCode = 1;

        final Long scheduleCode = projectScheduleService.save(projectCode, scheduleRequest, memberCode);

        return ResponseEntity.created(URI.create("/schedule-management" + scheduleCode)).build();
    }

    /* 일정글 수정 */
    @PutMapping ("/{projectCode}/schedule/{scheduleCode}")
    public ResponseEntity<Void> update(@PathVariable @Valid final Long projectCode,
                                     @PathVariable @Valid final Long scheduleCode,
                                     @RequestBody @Valid final ProjectScheduleUpdateRequest scheduleRequest) {
        int memberCode = 1;

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

}

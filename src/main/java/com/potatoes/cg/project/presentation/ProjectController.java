package com.potatoes.cg.project.presentation;

import com.potatoes.cg.common.paging.Pagenation;
import com.potatoes.cg.common.paging.PagingButtonInfo;
import com.potatoes.cg.common.paging.PagingResponse;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.project.dto.request.ProjectCreateRequest;
import com.potatoes.cg.project.dto.response.ProjectResponse;
import com.potatoes.cg.project.dto.response.ProjectsResponse;
import com.potatoes.cg.project.service.ProjectService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cg-api/v1")
public class ProjectController {

    @Autowired
    private final ProjectService projectService;

    /* 프로젝트 생성 */
    @PostMapping("/projects")
    public ResponseEntity<Void> save(@RequestBody @Valid final ProjectCreateRequest projectRequest
    ) {
        int memberCode = 2;

        final Long projectCode = projectService.save(projectRequest, memberCode);

        return ResponseEntity.created(URI.create("/project/" + projectCode)).build();
    }

    /* 내 부서 프로젝트 조회 */
    @GetMapping("/projects/dept/{deptCode}")
    public ResponseEntity<PagingResponse> getMyDeptProjects(
            @RequestParam(defaultValue = "1") final Integer page,
            @PathVariable final Long deptCode) {

        final Page<ProjectsResponse> projects = projectService.getMyDeptProjects(page, deptCode);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(projects);
        final PagingResponse pagingResponse = PagingResponse.of(projects.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /* 내가 참여 중인 프로젝트 조회 */
    @GetMapping("/projects/myProjects/{memberCode}")
    public ResponseEntity<PagingResponse> getMyProjects(
            @RequestParam(defaultValue = "1") final Integer page,
            @PathVariable final Long memberCode) {

        final Page<ProjectsResponse> projets = projectService.getMyProjects(page, memberCode);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(projets);
        final PagingResponse pagingResponse = PagingResponse.of(projets.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }


    /* 프로젝트에 참여한 멤버 수 조회 */

    @GetMapping("/projects/{projectCode}/participantCount")
    public ResponseEntity<Long> getParticipantCount(@PathVariable final Long projectCode) {
        try {
            // 프로젝트에 참여한 멤버 수 조회
            long participantCount = projectService.countParticipantsByProjectCode(projectCode);

            // 클라이언트에게 멤버 수 반환
            return ResponseEntity.ok(participantCount);
        } catch (EntityNotFoundException e) {
            // 프로젝트가 없는 경우 404 에러 반환
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // 다른 예외가 발생한 경우 500 에러 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

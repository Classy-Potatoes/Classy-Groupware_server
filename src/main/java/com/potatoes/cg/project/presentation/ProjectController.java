package com.potatoes.cg.project.presentation;

import com.potatoes.cg.common.paging.Pagenation;
import com.potatoes.cg.common.paging.PagingButtonInfo;
import com.potatoes.cg.common.paging.PagingResponse;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.ProjectParticipantId;
import com.potatoes.cg.project.dto.request.*;
import com.potatoes.cg.project.dto.response.MemberDeptResponse;
import com.potatoes.cg.project.dto.response.ProjectPostResponse;
import com.potatoes.cg.project.dto.response.ProjectResponse;
import com.potatoes.cg.project.dto.response.ProjectsResponse;
import com.potatoes.cg.project.service.PostService;
import com.potatoes.cg.project.service.ProjectMemberService;
import com.potatoes.cg.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cg-api/v1")
@Slf4j
public class ProjectController {

    @Autowired
    private final ProjectService projectService;

    private final PostService postService;

    private final ProjectMemberService projectMemberService;

    /* 프로젝트 생성 - 부장이상 */
    @PostMapping("/projects")
    public ResponseEntity<Void> save(@RequestBody @Valid final ProjectCreateRequest projectRequest,
                                     @AuthenticationPrincipal final CustomUser customUser
    ) {

        final Long projectCode = projectService.save(projectRequest, customUser);

        return ResponseEntity.created(URI.create("/projects/" + projectCode)).build();
    }

    /* 내 부서 프로젝트 조회 */
    @GetMapping("/projects/myDept")
    public ResponseEntity<PagingResponse> getMyDeptProjects(
            @RequestParam(defaultValue = "1") final Integer page,
            @AuthenticationPrincipal final CustomUser customUser
    ) {

        // 프로젝트 서비스를 통해 부서 프로젝트 조회
        final Page<ProjectsResponse> projects = projectService.getMyDeptProjects(page, customUser);

        // 페이징 정보 구성
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(projects);

        // 응답 구성
        final PagingResponse pagingResponse = PagingResponse.of(projects.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }



    /* 내가 참여 중인 프로젝트 조회 */
    @GetMapping("/projects/myProjects")
    public ResponseEntity<PagingResponse> getMyProjects(
            @RequestParam(defaultValue = "1") final Integer page,
            @AuthenticationPrincipal final CustomUser customUser) {

        final Page<ProjectsResponse> projets = projectService.getMyProjects(page, customUser);
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

    /* 프로젝트 디테일 */
    @GetMapping("/projects/{projectCode}")
    public ResponseEntity<ProjectResponse> getProjectDetail(@PathVariable final Long projectCode) {

        final ProjectResponse projectResponse = projectService.getProjectDetail(projectCode);

        return ResponseEntity.ok(projectResponse);
    }

    /* 프로젝트 수정 - 부장 이상 */
    @PutMapping("/projects/{projectCode}")
    public ResponseEntity<Void> projectUpdate(@PathVariable final Long projectCode,
                                              @RequestBody @Valid final ProjectUpdateRequest projectRequest) {

        projectService.projectUpdate(projectCode, projectRequest);

        return ResponseEntity.created(URI.create("/projects/" + projectCode)).build();
    }

    /* 프로젝트 삭제 - 부장 이상 */
    @DeleteMapping("/projects/{projectCode}")
    public ResponseEntity<Void> projectDelete(@PathVariable final Long projectCode) {

        projectService.projectDelete(projectCode);

        return ResponseEntity.noContent().build();
    }

    /* 부서별 회원 조회 */
    @GetMapping("/dept/{deptCode}/member")
    public ResponseEntity<PagingResponse> getDeptMember(
            @RequestParam(defaultValue = "1") final Integer page, @PathVariable final Long deptCode
    )    {

        final Page<MemberDeptResponse> members = projectMemberService.getDeptMember(page, deptCode);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(members);
        final PagingResponse pagingResponse = PagingResponse.of(members.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /* 프로젝트 회원 초대하기 */
//    @PostMapping("/invite")
//    public ResponseEntity<Void> inviteMember(
//            @RequestBody @Valid final ProjectInviteMemberRequest projectInviteMemberRequest
//    ) {
//
//        final ProjectParticipantId id = projectMemberService.inviteMember(projectInviteMemberRequest);
//
//        return ResponseEntity.created(URI.create("/invite" + id)).build();
//
//    }
    @PostMapping("/invite")
    public ResponseEntity<List<ProjectParticipantId>> inviteMembers(
            @RequestBody @Valid final List<ProjectInviteMemberRequest> projectInviteMemberRequests
    ) {
        List<ProjectParticipantId> invitedMembers = projectMemberService.inviteMembers(projectInviteMemberRequests);

        return ResponseEntity.created(URI.create("/invite")).body(invitedMembers);
    }


    /* ----------------------------------------------------------------------------------------------------------- */


    /* 프로젝트 일반 게시글 작성 */
    @PostMapping("/post")
    public ResponseEntity<Void> postSave(
            @RequestPart @Valid final ProjectPostCreateRequest projectPostRequest,
            @AuthenticationPrincipal final CustomUser customUser,
            @RequestPart(required = false) final List<MultipartFile> attachment

    ) {

        final Long postCode = postService.postSave(projectPostRequest, customUser, attachment);

        return ResponseEntity.created(URI.create("/post/" + postCode)).build();
    }

//    /* 프로젝트 일반 게시글 수정 */
//    @PutMapping("/post/{postCode}")
//    public ResponseEntity<Void> postUpdate(@PathVariable final Long postCode,
//                                           @RequestPart @Valid final PostUpdateRequest postUpdateRequest,
//                                           @RequestPart(required = false) final List<MultipartFile> attachment,
//                                           @AuthenticationPrincipal final CustomUser customUser) {
//
//        postService.postUpdate(postCode, postUpdateRequest, attachment, customUser);
//
//        return ResponseEntity.created(URI.create("/post/" + postCode)).build();
//
//    }

    /* 프로젝트 일반 글 삭제  */
    @DeleteMapping("/post/{postCode}")
    public ResponseEntity<Void> postDelete(@PathVariable final Long postCode) {

        postService.postDelete(postCode);

        return ResponseEntity.noContent().build();
    }


    /* 프로젝트 일반 게시글 디테일 */
    @GetMapping("/post/{postCode}")
    public ResponseEntity<ProjectPostResponse> getProjectPost(@PathVariable final Long postCode){

        final ProjectPostResponse projectPostResponse = postService.getPostDetail(postCode);

        return ResponseEntity.ok(projectPostResponse);
    }
}

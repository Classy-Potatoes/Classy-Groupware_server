package com.potatoes.cg.project.presentation;

import com.potatoes.cg.common.paging.Pagenation;
import com.potatoes.cg.common.paging.PagingButtonInfo;
import com.potatoes.cg.common.paging.PagingResponse;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.ProjectParticipantId;
import com.potatoes.cg.project.dto.request.*;
import com.potatoes.cg.project.dto.response.*;
import com.potatoes.cg.project.service.*;
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
import java.util.Map;
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

    private final ReplyService replyService;

    private final TaskService taskService;

    /* 프로젝트 생성 - 부장이상 */
    @PostMapping("/projects")
    public ResponseEntity<Void> save(@RequestBody @Valid final ProjectCreateRequest projectRequest,
                                     @AuthenticationPrincipal final CustomUser customUser
    ) {

        final Long projectCode = projectService.save(projectRequest, customUser);

        return ResponseEntity.created(URI.create("/projects/" + projectCode)).build();
    }

    /* 회원 초대하기 */
    @PostMapping("/invite")
    public ResponseEntity<List<ProjectParticipantId>> inviteMembers(
            @RequestBody Map<String, List<Map<String, Long>>> requestBody
    ) {
        List<Map<String, Long>> membersData = requestBody.get("members");

        List<ProjectInviteMemberRequest> projectInviteMemberRequests = membersData.stream()
                .map(memberData -> new ProjectInviteMemberRequest(
                        memberData.get("projectCode"),
                        memberData.get("memberCode")
                ))
                .collect(Collectors.toList());

        List<ProjectParticipantId> invitedMembers = projectMemberService.inviteMembers(projectInviteMemberRequests);

        return ResponseEntity.created(URI.create("/invite")).body(invitedMembers);
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

        final Page<ProjectsResponse> projects = projectService.getMyProjects(page, customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(projects);
        final PagingResponse pagingResponse = PagingResponse.of(projects.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }


    /* 프로젝트에 참여한 멤버 수 조회 */
//    @GetMapping("/projects/{projectCode}/participantCount")
//    public ResponseEntity<Long> getParticipantCount(@PathVariable final Long projectCode) {
//        try {
//            // 프로젝트에 참여한 멤버 수 조회
//            long participantCount = projectService.countParticipantsByProjectCode(projectCode);
//
//            // 클라이언트에게 멤버 수 반환
//            return ResponseEntity.ok(participantCount);
//        } catch (EntityNotFoundException e) {
//            // 프로젝트가 없는 경우 404 에러 반환
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            // 다른 예외가 발생한 경우 500 에러 반환
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

//    /* 내 프로젝트와 그 프로젝트의 참여자 수 조회 */
//    @GetMapping("/projects/myProjectsAndMemberCount")
//    public ResponseEntity<PagingResponse> getMyProjectsAndParticipantCount(
//            @RequestParam(defaultValue = "1") final Integer page,
//            @AuthenticationPrincipal final CustomUser customUser) {
//
//        final Page<ProjectsResponse> projects = projectService.getMyProjects(page, customUser);
//
//
//        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(projects);
//        final PagingResponse pagingResponse = PagingResponse.of(projects.getContent(), pagingButtonInfo);
//
//        return ResponseEntity.ok(pagingResponse);
//    }


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
//    @GetMapping("/dept/{deptCode}/member")
//    public ResponseEntity<List<MemberDeptResponse>> getDeptMember(
//            @PathVariable final Long deptCode) {
//
//        List<MemberDeptResponse> projectMemberResponseList = projectMemberService.getDeptMember(deptCode);
//
//        return ResponseEntity.status(HttpStatus.OK).body(projectMemberResponseList);
//    }
    @GetMapping("/dept/{deptCode}/project/{projectCode}/member")
    public ResponseEntity<List<MemberDeptResponse>> getDeptMember(
            @PathVariable final Long deptCode,
            @PathVariable final Long projectCode) {

        List<MemberDeptResponse> projectMemberResponseList = projectMemberService.getDeptMember(deptCode,projectCode);

        return ResponseEntity.status(HttpStatus.OK).body(projectMemberResponseList);
    }


    /* 부서별 회원 검색 */
    @GetMapping("/dept/{deptCode}/project/{projectCode}/search")
    public ResponseEntity<List<MemberDeptResponse>> getDeptSearch(
            @PathVariable final Long deptCode,
            @PathVariable final Long projectCode,
            @RequestParam final String infoName
    )    {

        List<MemberDeptResponse> members = projectMemberService.getDeptSearch(deptCode, infoName, projectCode);


        return ResponseEntity.status(HttpStatus.OK).body(members);

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


    /* 회원 검색 (초대) */
    @GetMapping("/invite/search")
    public ResponseEntity<PagingResponse> getInviteMemberSearch(@RequestParam(defaultValue = "1") final Integer page,
                                                                @RequestParam final String infoName){

        final Page<ProjectMemberResponse> members = projectMemberService.getInviteMemberSearch(page, infoName);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(members);
        final PagingResponse pagingResponse = PagingResponse.of(members.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }



    /* 프로젝트 인원 조회(이름) */
    @GetMapping("/projects/{projectCode}/searchMember")
    public ResponseEntity<List<ProjectMemberResponse>> getMemberList(@PathVariable final Long projectCode) {

        List<ProjectMemberResponse> projectMemberResponseList = projectMemberService.getMemberList(projectCode);

        return ResponseEntity.status(HttpStatus.OK).body(projectMemberResponseList);
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


    /* 프로젝트 일반 게시글 수정 */
    @PutMapping("/post/{postCode}")
    public ResponseEntity<Void> postUpdate(@PathVariable final Long postCode,
                                           @RequestBody @Valid final PostUpdateRequest postUpdateRequest
                                       ) {

        postService.postUpdate(postCode, postUpdateRequest);

        return ResponseEntity.created(URI.create("/post/" + postCode)).build();

    }

    /* 프로젝트 일반 글 삭제  */
    @DeleteMapping("/post/{postCode}")
    public ResponseEntity<Void> postDelete(@PathVariable final Long postCode) {

        postService.postDelete(postCode);

        return ResponseEntity.noContent().build();
    }


    /* 프로젝트 일반 게시글 디테일 */
//    @GetMapping("/post/{postCode}")
//    public ResponseEntity<ProjectPostResponse> getProjectPost(@PathVariable final Long postCode){
//
//        final ProjectPostResponse projectPostResponse = postService.getPostDetail(postCode);
//
//        return ResponseEntity.ok(projectPostResponse);
//    }

    /* 프로젝트 일반 게시글 조회 */
    @GetMapping("/projects/{projectCode}/post")
    public ResponseEntity<PagingResponse> getPost(
            @RequestParam(defaultValue = "1") final Integer page,
            @PathVariable final Long projectCode,
            @AuthenticationPrincipal final CustomUser customUser
           ) {

        final Page<ProjectPostResponse> posts = postService.getPostsDetail(page, projectCode, customUser);

        // 페이징 정보 구성
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(posts);

        // 응답 구성
        final PagingResponse pagingResponse = PagingResponse.of(posts.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }


    /* 프로젝트 일반 게시글 댓글 */
    @PostMapping("/post/reply")
    public ResponseEntity<Void> postReplySave(
            @RequestBody @Valid final ReplyCreateRequest replyCreateRequest,
            @AuthenticationPrincipal final CustomUser customUser

    ) {

        final Long replyCode = replyService.postReplySave(replyCreateRequest, customUser);

        return ResponseEntity.created(URI.create("/post/reply/" + replyCode)).build();
    }


    /* 프로젝트 일반 게시글 댓글 조회 */
    @GetMapping("/post/{postCode}/reply")
    public ResponseEntity<PagingResponse> getPostReply(
                                    @RequestParam(defaultValue = "1") final Integer page,
                                    @PathVariable final Long postCode) {

        // 댓글 서비스를 통해 댓글 조회
        final Page<PostReplyResponse> postReplys = replyService.getPostReply(page, postCode);

        // 페이징 정보 구성
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(postReplys);

        // 응답 구성
        final PagingResponse pagingResponse = PagingResponse.of(postReplys.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /* 프로젝트 일반 게시글 댓글 수정 */
    @PutMapping("/reply/{replyCode}")
    public ResponseEntity<Void> postReplyUpdate(@PathVariable final Long replyCode,
                                                      @RequestBody @Valid final ReplyUpdateRequest replyRequest) {

        replyService.postReplyUpdate(replyCode, replyRequest);

        return ResponseEntity.created(URI.create("/post/reply/" + replyCode)).build();

    }


    /* 프로젝트 일반 게시글 댓글 삭제 */
    @DeleteMapping("/reply/{replyCode}")
    public ResponseEntity<Void> postReplyDelete(@PathVariable final Long replyCode) {

        replyService.postReplyDelete(replyCode);

        return ResponseEntity.noContent().build();
    }

    /*-----------------------------------------------------------------------------------------------------------*/

    /* 프로젝트 업무 작성 */

    @PostMapping("/task")
    public ResponseEntity<Void> taskSave(
            @RequestPart @Valid final ProjectTaskCreateRequest projecttaskRequest,
            @AuthenticationPrincipal final CustomUser customUser,
            @RequestPart(required = false) final List<MultipartFile> attachment

    ) {

        final Long taskCode = taskService.taskSave(projecttaskRequest, customUser, attachment);

        return ResponseEntity.created(URI.create("/task/" + taskCode)).build();
    }


    /* 프로젝트 업무 수정 */
    @PutMapping("/task/{taskCode}")
    public ResponseEntity<Void> taskUpdate(@PathVariable final Long taskCode,
                                           @RequestBody @Valid final TaskUpdateRequest TaskUpdateRequest
    ) {

        taskService.taskUpdate(taskCode, TaskUpdateRequest);

        return ResponseEntity.created(URI.create("/task/" + taskCode)).build();

    }

    /* 프로젝트 업무 삭제 */
    @DeleteMapping("/task/{taskCode}")
    public ResponseEntity<Void> taskDelete(@PathVariable final Long taskCode) {

        taskService.taskDelete(taskCode);

        return ResponseEntity.noContent().build();
    }

    /* 업무 댓글 작성 */
    @PostMapping("/task/reply")
    public ResponseEntity<Void> taskReplySave(
            @RequestBody @Valid final ReplyCreateRequest replyCreateRequest,
            @AuthenticationPrincipal final CustomUser customUser

    ) {

        final Long replyCode = replyService.taskReplySave(replyCreateRequest, customUser);

        return ResponseEntity.created(URI.create("/task/reply/" + replyCode)).build();
    }

    /* 내 업무 조회 */
    @GetMapping("/task/myTask")
    public ResponseEntity<PagingResponse> getMyTask(
            @RequestParam(defaultValue = "1") final Integer page,
            @AuthenticationPrincipal final CustomUser customUser) {

        final Page<MyTaskResponse> tasks = taskService.getMyTask(page, customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(tasks);
        final PagingResponse pagingResponse = PagingResponse.of(tasks.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /* 업무 조회 */
    @GetMapping("/projects/{projectCode}/task")
    public ResponseEntity<PagingResponse> getTask(
            @RequestParam(defaultValue = "1") final Integer page,
            @PathVariable final Long projectCode,
            @AuthenticationPrincipal final CustomUser customUser
    ) {

        final Page<ProjectTaskResponse> tasks = taskService.getTaskDetail(page, projectCode, customUser);

        // 페이징 정보 구성
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(tasks);

        // 응답 구성
        final PagingResponse pagingResponse = PagingResponse.of(tasks.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

//    /* 업무 요청 확인하기 */
//    @PutMapping("/")


}

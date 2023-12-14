package com.potatoes.cg.project.service;


import com.potatoes.cg.approval.domain.ApprovalFile;
import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.common.util.MultipleFileUploadUtils;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.Dept;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.ProjectParticipant;
import com.potatoes.cg.project.domain.ProjectParticipantId;
import com.potatoes.cg.project.domain.ProjectPost;
import com.potatoes.cg.project.domain.repository.*;
import com.potatoes.cg.project.dto.request.ProjectCreateRequest;
import com.potatoes.cg.project.dto.request.ProjectInviteMemberRequest;
import com.potatoes.cg.project.dto.request.ProjectPostCreateRequest;
import com.potatoes.cg.project.dto.request.ProjectUpdateRequest;
import com.potatoes.cg.project.dto.response.MemberDeptResponse;
import com.potatoes.cg.project.dto.response.ProjectResponse;
import com.potatoes.cg.project.dto.response.ProjectsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.potatoes.cg.common.exception.type.ExceptionCode.*;
import static com.potatoes.cg.member.domain.type.MemberStatus.ACTIVE;
import static com.potatoes.cg.project.domain.type.ProjectStatusType.DELETED;
import static com.potatoes.cg.project.domain.type.ProjectStatusType.USABLE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectDeptRepository projectDeptRepository;
    private final ProjectParticipantRepository projectParticipantRepository;
    private final InfoRepository infoRepository;


    /* 뉴 프로젝트 생성 */
    public Long save(final ProjectCreateRequest projectRequest, final CustomUser customUser) {


        Dept dept = projectDeptRepository.getReferenceById(projectRequest.getDeptCode());

        final Project newProject = Project.of(
                projectRequest.getProjectTitle(),
                projectRequest.getProjectBody(),
                projectRequest.getProjectStartDate(),
                projectRequest.getProjectEndDate(),
                dept,
                customUser.getInfoCode()
        );

        // 프로젝트 저장
        final Project project = projectRepository.save(newProject);

        // 생성자를 참여자로 추가
        addParticipant(project, customUser.getInfoCode());

        return project.getProjectCode();
    }

    /* 참여자 추가 메서드 */
    private void addParticipant(Project project, Long infoCode) {
        MemberInfo member = infoRepository.getReferenceById(infoCode);

        final ProjectParticipant newProjectParticipant = ProjectParticipant.of(
                project,
                member
        );

        projectParticipantRepository.save(newProjectParticipant);
    }



    /* 내 부서 프로젝트 조회 */

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("projectCode").descending());
    }

    public Page<ProjectsResponse> getMyDeptProjects(final Integer page, final CustomUser customUser) {

        MemberInfo info = infoRepository.getReferenceById( customUser.getInfoCode());

        Page<Project> projects = projectRepository.findByDeptDeptCodeAndProjectStatus(getPageable(page), info.getDept().getDeptCode(), USABLE);

        return projects.map(project -> {

            // 프로젝트에 참여한 멤버 수 조회
            long participantCount = projectRepository.countParticipantsByProjectCode(project.getProjectCode());

            // ProjectsResponse 생성자를 이용하여 새로운 ProjectsResponse 인스턴스 생성
            return ProjectsResponse.from(project, participantCount);
        });
    }


    /* 내가 참여 중인 프로젝트 조회 */
    @Transactional(readOnly = true)
    public Page<ProjectsResponse> getMyProjects(final Integer page, final CustomUser customUser) {

        Page<Project> projects = projectRepository.findMyProjectsAndProjectStatus(getPageable(page), customUser.getInfoCode(), USABLE);


        return projects.map(project -> {
            // 프로젝트에 참여한 멤버 수 조회
            long participantCount = projectRepository.countParticipantsByProjectCode(project.getProjectCode());

            // ProjectsResponse 생성자를 이용하여 새로운 ProjectsResponse 인스턴스 생성
            return ProjectsResponse.from(project, participantCount);
        });
    }

//
//    /* 프로젝트에 참여인원 조회 */
//    public long countParticipantsByProjectCode(final Long projectCode) {
//
//        return projectRepository.countParticipantsByProjectCode(projectCode);
//    }

    /* 프로젝트 디테일 조회 */
    @Transactional(readOnly = true)
    public ProjectResponse getProjectDetail(final Long projectCode) {

        Project project = projectRepository.findByProjectCodeAndProjectStatus(projectCode, USABLE)
                .orElseThrow(() -> new NotFoundException(NOT_PROJECT_CODE));

        return ProjectResponse.from(project);
    }

    /* 프로젝트 수정 */
    public void projectUpdate(final Long projectCode, final ProjectUpdateRequest projectRequest) {

        Project project = projectRepository.findByProjectCodeAndProjectStatusNot(projectCode, DELETED)
                .orElseThrow(() -> new NotFoundException(NOT_PROJECT_CODE));

        Dept dept = projectDeptRepository.findById(projectRequest.getDeptCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_DEPT_CODE));

        project.projectUpdate(
                projectRequest.getProjectTitle(),
                projectRequest.getProjectBody(),
                projectRequest.getProjectStartDate(),
                projectRequest.getProjectEndDate(),
                dept
        );

    }

    /* 프로젝트 삭제 */
    public void projectDelete(final Long projectCode) {

        projectRepository.deleteById(projectCode);
    }

}




package com.potatoes.cg.project.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.member.domain.Member;

import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.ProjectParticipant;
import com.potatoes.cg.project.domain.ProjectParticipantId;
import com.potatoes.cg.project.domain.repository.ProjectParticipantRepository;
import com.potatoes.cg.project.domain.repository.ProjectRepository;
import com.potatoes.cg.project.dto.request.ProjectInviteMemberRequest;
import com.potatoes.cg.project.dto.response.MemberDeptResponse;
import com.potatoes.cg.project.dto.response.ProjectMemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.potatoes.cg.common.exception.type.ExceptionCode.NOT_FOUND_INFO_CODE;
import static com.potatoes.cg.common.exception.type.ExceptionCode.NOT_PROJECT_CODE;
import static com.potatoes.cg.member.domain.type.MemberStatus.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectMemberService {

    private final ProjectmemberRepository projectMemberRepository;
    private final InfoRepository infoRepository;
    private final ProjectParticipantRepository projectParticipantRepository;
    private final ProjectRepository projectRepository;

    private Pageable getPageable(final Integer page) {
//        return PageRequest.of(page - 1, 5, Sort.by("memberInfo").descending());
        return PageRequest.of(page - 1, 5);
    }

    /* 부서별 회원 조회 */
    public Page<MemberDeptResponse> getDeptMember(final Integer page, final Long deptCode) {

        Page<Member> members = projectMemberRepository.findByMemberInfoDeptDeptCodeAndMemberStatus(getPageable(page), deptCode, ACTIVE);

        return members.map(Member -> MemberDeptResponse.from(Member));
    }

    /* 프로젝트에 회원 초대 */
    public List<ProjectParticipantId> inviteMembers(List<ProjectInviteMemberRequest> projectInviteMemberRequests) {
        List<ProjectParticipantId> invitedMembers = new ArrayList<>();

        for (ProjectInviteMemberRequest request : projectInviteMemberRequests) {
            MemberInfo member = infoRepository.getReferenceById(request.getMemberCode());

            Project project = projectRepository.getReferenceById(request.getProjectCode());

            final ProjectParticipant newProjectParticipant = ProjectParticipant.of(
                    project,
                    member
            );

            final ProjectParticipant projectParticipant = projectParticipantRepository.save(newProjectParticipant);
            invitedMembers.add(projectParticipant.getId());
        }

        return invitedMembers;
    }

    //    public ProjectParticipantId inviteMember(ProjectInviteMemberRequest projectInviteMemberRequest) {
//
//        MemberInfo member = infoRepository.findById(projectInviteMemberRequest.getMemberCode())
//                .orElseThrow(() -> new NotFoundException(NOT_FOUND_INFO_CODE));
//
//        Project project = projectRepository.findById(projectInviteMemberRequest.getProjectCode())
//                .orElseThrow(() -> new NotFoundException(NOT_PROJECT_CODE));
//
//        final ProjectParticipant newProjectParticipant = ProjectParticipant.of(
//                project,
//                member
//        );
//
//        final ProjectParticipant projectParticipant = projectParticipantRepository.save(newProjectParticipant);
//
//        return projectParticipant.getId();
//    }

    /* 프로젝트 번호로 인원 조회 */
    @Transactional(readOnly = true)
    public List<ProjectMemberResponse> getMemberList(Long projectCode) {

        List<ProjectParticipant> projectParticipantList = projectParticipantRepository.findAllByProjectProjectCode(projectCode);

        List<ProjectMemberResponse> projectMemberResponseList = projectParticipantList.stream()
                .map(projectParticipant -> ProjectMemberResponse.from(
                        projectParticipant.getMember().getInfoCode(),
                        projectParticipant.getMember().getInfoName()
                )).collect(Collectors.toList());

        return projectMemberResponseList;
    }

    @Transactional(readOnly = true)
    /* 회원 검색 (초대) */
    public Page<ProjectMemberResponse> getInviteMemberSearch(Integer page, String infoName) {

        Page<Member> members = projectMemberRepository.findByMemberInfoInfoNameContainsAndMemberStatus(getPageable(page), infoName, ACTIVE);

        return members.map(Member -> ProjectMemberResponse.fromMember(Member));

    }
}

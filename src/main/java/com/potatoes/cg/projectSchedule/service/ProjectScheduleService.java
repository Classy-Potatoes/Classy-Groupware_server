package com.potatoes.cg.projectSchedule.service;

import com.potatoes.cg.calendar.domain.type.StatusType;
import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.member.domain.repository.MemberRepository;
import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.ProjectParticipant;
import com.potatoes.cg.project.domain.repository.ProjectParticipantRepository;
import com.potatoes.cg.project.domain.repository.ProjectRepository;
import com.potatoes.cg.project.dto.request.ProjectInviteMemberRequest;
import com.potatoes.cg.project.service.ProjectMemberService;
import com.potatoes.cg.project.service.ProjectService;
import com.potatoes.cg.projectManagers.domain.ProjectManagers;
import com.potatoes.cg.projectManagers.domain.repository.ProjectManagersRepository;
import com.potatoes.cg.projectSchedule.domain.ProjectSchedule;
import com.potatoes.cg.projectSchedule.domain.repository.ProjectScheduleRepository;
import com.potatoes.cg.projectSchedule.dto.request.ProjectManagerCreateRequest;
import com.potatoes.cg.projectSchedule.dto.request.ProjectScheduleCreatRequest;
import com.potatoes.cg.projectSchedule.dto.request.ProjectScheduleUpdateRequest;
import com.potatoes.cg.projectSchedule.dto.response.ProjectMemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.potatoes.cg.calendar.domain.type.StatusType.DELETED;
import static com.potatoes.cg.common.exception.type.ExceptionCode.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProjectScheduleService {

    private final MemberRepository memberRepository;
    private final ProjectService projectService;
    private final ProjectScheduleRepository projectScheduleRepository;
    private final ProjectParticipantRepository projectParticipantRepository;
    private final ProjectMemberService projectMemberService;
    private final ProjectManagersRepository projectManagersRepository;
    private final ProjectRepository projectRepository;
    private final InfoRepository infoRepository;

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

    /* 일정글 등록 */
    public Long save(Long projectCode, ProjectScheduleCreatRequest scheduleRequest, int memberCode) {

        final List<ProjectManagers> projectManagerList = memberRepository
                .findAllById(scheduleRequest.getAttendants()).stream().map(
                        member -> ProjectManagers.of(member)
                ).collect(Collectors.toList());

        LocalDate setStartDate = scheduleRequest.getScheduleStartedDate();
        LocalTime setStartTime = scheduleRequest.getScheduleStartedTime();
        LocalDate setEndDate = scheduleRequest.getScheduleEndDate();
        LocalTime setEndTime = scheduleRequest.getScheduleEndTime();

        LocalDateTime scheduleStartedDate = LocalDateTime.parse(setStartDate + "T" + setStartTime);
        LocalDateTime scheduleEndDate = LocalDateTime.parse(setEndDate + "T" + setEndTime);

        final Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROJECT_CODE));

        final ProjectSchedule newProjectSchedule = ProjectSchedule.of(
                scheduleRequest.getScheduleTitle(),
                scheduleRequest.getScheduleContent(),
                scheduleStartedDate,
                scheduleEndDate,
                project,
                projectManagerList
        );

        final ProjectSchedule projectSchedule = projectScheduleRepository.save(newProjectSchedule);

        return projectSchedule.getScheduleCode();


    }

    /* 일정글 수정 */
    public void update(Long projectCode, Long scheduleCode, ProjectScheduleUpdateRequest scheduleRequest) {

        ProjectSchedule projectSchedule = projectScheduleRepository.findByScheduleCodeAndScheduleStatusNot(scheduleCode, DELETED)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_SCHEDULE_CODE));

        projectManagersRepository.deleteAll(projectSchedule.getProjectManagerList());
        final List<ProjectManagers> projectManagerList = memberRepository
                .findAllById(scheduleRequest.getAttendants()).stream().map(
                        member -> ProjectManagers.of(member)
                ).collect(Collectors.toList());

        LocalDate setStartDate = scheduleRequest.getScheduleStartedDate();
        LocalTime setStartTime = scheduleRequest.getScheduleStartedTime();
        LocalDate setEndDate = scheduleRequest.getScheduleEndDate();
        LocalTime setEndTime = scheduleRequest.getScheduleEndTime();

        LocalDateTime scheduleStartedDate = LocalDateTime.parse(setStartDate + "T" + setStartTime);
        LocalDateTime scheduleEndDate = LocalDateTime.parse(setEndDate + "T" + setEndTime);

        final Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROJECT_CODE));

        projectSchedule.update(
                scheduleRequest.getScheduleTitle(),
                scheduleRequest.getScheduleContent(),
                scheduleStartedDate,
                scheduleEndDate,
                project,
                projectManagerList
        );
    }

    /* 일정글 삭제 */
    public void delete(Long projectCode, Long scheduleCode) {

        ProjectSchedule projectSchedule = projectScheduleRepository.findByScheduleCode(scheduleCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_SCHEDULE_CODE));

        projectManagersRepository.deleteAll(projectSchedule.getProjectManagerList());
        projectScheduleRepository.deleteById(scheduleCode);
    }
}

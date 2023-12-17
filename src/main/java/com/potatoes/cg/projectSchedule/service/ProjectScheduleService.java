package com.potatoes.cg.projectSchedule.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.common.exception.ServerInternalException;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.member.domain.repository.MemberRepository;
import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.ProjectReply;
import com.potatoes.cg.project.domain.repository.ProjectParticipantRepository;
import com.potatoes.cg.project.domain.repository.ProjectReplyRepository;
import com.potatoes.cg.project.domain.repository.ProjectRepository;
import com.potatoes.cg.project.domain.type.ProjectStatusType;
import com.potatoes.cg.project.service.ProjectMemberService;
import com.potatoes.cg.project.service.ProjectService;
import com.potatoes.cg.projectManagers.domain.ProjectManagersSchedule;
import com.potatoes.cg.projectManagers.domain.repository.ProjectManagersScheduleRepository;
import com.potatoes.cg.projectSchedule.domain.ProjectSchedule;
import com.potatoes.cg.projectSchedule.domain.repository.ProjectScheduleRepository;
import com.potatoes.cg.projectSchedule.dto.request.ProjectScheduleCreatRequest;
import com.potatoes.cg.projectSchedule.dto.request.SchReplyUpdate;
import com.potatoes.cg.projectSchedule.dto.request.ScheduleReplyCreateRequest;
import com.potatoes.cg.projectSchedule.dto.request.ProjectScheduleUpdateRequest;
import com.potatoes.cg.projectSchedule.dto.response.ProjectScheduleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.potatoes.cg.calendar.domain.type.StatusType.DELETED;
import static com.potatoes.cg.common.exception.type.ExceptionCode.*;
import static com.potatoes.cg.project.domain.type.ProjectOptionType.SCHEDULE;
import static com.potatoes.cg.project.domain.type.ProjectStatusType.USABLE;

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
    private final ProjectManagersScheduleRepository projectManagersScheduleRepository;
    private final ProjectRepository projectRepository;
    private final InfoRepository infoRepository;
    private final ProjectReplyRepository projectReplyRepository;

//    /* 프로젝트 번호로 인원 조회 */
//    @Transactional(readOnly = true)
//    public List<ProjectMemberResponse> getMemberList(Long projectCode) {
//
//        List<ProjectParticipant> projectParticipantList = projectParticipantRepository.findAllByProjectProjectCode(projectCode);
//
//        List<ProjectMemberResponse> projectMemberResponseList = projectParticipantList.stream()
//                .map(projectParticipant -> ProjectMemberResponse.from(
//                        projectParticipant.getMember().getInfoCode(),
//                        projectParticipant.getMember().getInfoName()
//                )).collect(Collectors.toList());
//
//        return projectMemberResponseList;
//    }

    /* 일정글 등록 */
    public Long save(Long projectCode, ProjectScheduleCreatRequest scheduleRequest, CustomUser customUser) {

//        final List<ProjectManagersSchedule> projectManagerList = memberRepository
//                .findAllById(scheduleRequest.getAttendants()).stream().map(
//                        member -> ProjectManagersSchedule.of(member)
//                ).collect(Collectors.toList());

        Member member = memberRepository.findById(customUser.getMemberCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER_CODE));

        List<MemberInfo> members = infoRepository.findAllById(scheduleRequest.getAttendants());

        List<Member> memberList = memberRepository.findAllByMemberInfoIn(members);
        log.info("aaaaaa : {}", memberList);

        List<ProjectManagersSchedule> projectManagersScheduleList = memberList.stream().map(
                newMem -> ProjectManagersSchedule.of(newMem)
        ).collect(Collectors.toList());

        log.info("sdfdf : {}", projectManagersScheduleList);

        LocalDate setStartDate = scheduleRequest.getScheduleStartedDate();
        LocalTime setStartTime = scheduleRequest.getScheduleStartedTime();
        LocalDate setEndDate = scheduleRequest.getScheduleEndDate();
        LocalTime setEndTime = scheduleRequest.getScheduleEndTime();

        if (setStartTime == null || setEndTime == null) {
            setStartTime = LocalTime.parse("00:00");
            setEndTime = LocalTime.parse("00:00");
        }

        if (setStartDate == null) {
            throw new NotFoundException(NOT_FOUND_VALID_DATE);
        }

        if (setEndDate == null) {
            setEndDate = setStartDate;
        }

        LocalDateTime scheduleStartedDate = LocalDateTime.parse(setStartDate + "T" + setStartTime);
        LocalDateTime scheduleEndDate = LocalDateTime.parse(setEndDate + "T" + setEndTime);

        if (scheduleRequest.getScheduleTitle() == null || scheduleRequest.getScheduleTitle().isEmpty()) {
            throw new NotFoundException(NOT_FOUND_VALID_TITLE);
        }

        if (scheduleStartedDate.isAfter(scheduleEndDate)) {
            throw new ServerInternalException(NOT_VALID_DATE);
        }

        if (scheduleRequest.getAttendants().size() < 1) {
            throw new NotFoundException(NOT_FOUND_MEMBER);
        }

        final Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROJECT_CODE));

        final ProjectSchedule newProjectSchedule = ProjectSchedule.of(
                scheduleRequest.getScheduleTitle(),
                scheduleRequest.getScheduleContent(),
                scheduleStartedDate,
                scheduleEndDate,
                member,
                project,
                projectManagersScheduleList
        );

        final ProjectSchedule projectSchedule = projectScheduleRepository.save(newProjectSchedule);

        return projectSchedule.getScheduleCode();

    }

    /* 일정글 수정 */
    public void update(Long projectCode, Long scheduleCode, ProjectScheduleUpdateRequest scheduleRequest) {

        ProjectSchedule projectSchedule = projectScheduleRepository.findByScheduleCodeAndScheduleStatusNot(scheduleCode, DELETED)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_SCHEDULE_CODE));

        projectManagersScheduleRepository.deleteAllByScheduleCode(projectSchedule.getScheduleCode());

//        final List<ProjectManagersSchedule> projectManagerList = memberRepository
//                .findAllById(scheduleRequest.getAttendants()).stream().map(
//                        member -> ProjectManagersSchedule.of(member)
//                ).collect(Collectors.toList());

        List<MemberInfo> members = infoRepository.findAllById(scheduleRequest.getAttendants());

        List<Member> memberList = memberRepository.findAllByMemberInfoIn(members);
        log.info("aaaaaa : {}", memberList);

        List<ProjectManagersSchedule> projectManagersScheduleList = memberList.stream().map(
                newMem -> ProjectManagersSchedule.of(newMem)
        ).collect(Collectors.toList());

//        Member member = memberRepository.findById(customUser.getMemberCode())
//                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER_CODE));

        LocalDate setStartDate = scheduleRequest.getScheduleStartedDate();
        LocalTime setStartTime = scheduleRequest.getScheduleStartedTime();
        LocalDate setEndDate = scheduleRequest.getScheduleEndDate();
        LocalTime setEndTime = scheduleRequest.getScheduleEndTime();

        if (setStartTime == null || setEndTime == null) {
            setStartTime = LocalTime.parse("00:00");
            setEndTime = LocalTime.parse("00:00");
        }

        if (setStartDate == null) {
            throw new NotFoundException(NOT_FOUND_VALID_DATE);
        }

        if (setEndDate == null) {
            setEndDate = setStartDate;
        }

        LocalDateTime scheduleStartedDate = LocalDateTime.parse(setStartDate + "T" + setStartTime);
        LocalDateTime scheduleEndDate = LocalDateTime.parse(setEndDate + "T" + setEndTime);

        if (scheduleRequest.getScheduleTitle() == null || scheduleRequest.getScheduleTitle().isEmpty()) {
            throw new NotFoundException(NOT_FOUND_VALID_TITLE);
        }

        if (scheduleStartedDate.isAfter(scheduleEndDate)) {
            throw new ServerInternalException(NOT_VALID_DATE);
        }

        if (scheduleRequest.getAttendants().size() < 1) {
            throw new NotFoundException(NOT_FOUND_MEMBER);
        }

        final Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROJECT_CODE));

        projectSchedule.update(
                scheduleRequest.getScheduleTitle(),
                scheduleRequest.getScheduleContent(),
                scheduleStartedDate,
                scheduleEndDate,
                project,
                projectManagersScheduleList
        );
    }

    /* 일정글 삭제 */
    public void delete(Long projectCode, Long scheduleCode) {

        ProjectSchedule projectSchedule = projectScheduleRepository.findByScheduleCodeAndProjectProjectCode(scheduleCode, projectCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_SCHEDULE_CODE));
//projectSchedule.getProjectManagerList()
//                .stream().map(code -> code.getScheduleCode()).collect(Collectors.toList())
        log.info("123123 : {}", projectSchedule.getScheduleCode());
        projectManagersScheduleRepository.deleteAllByScheduleCode(projectSchedule.getScheduleCode());
        projectScheduleRepository.deleteById(scheduleCode);
    }

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("scheduleCode").descending());
    }

    /* 일정글 조회 */
    @Transactional(readOnly = true)
    public Page<ProjectScheduleResponse> getSchedule(Integer page, Long projectCode, CustomUser customUser) {

        Page<ProjectSchedule> projectSchedules = projectScheduleRepository.findByProjectProjectCodeAndScheduleStatusNotAndMemberMemberCode(projectCode, getPageable(page), DELETED, customUser.getMemberCode());

        log.info("xvsvsds : {}", projectSchedules);
        return projectSchedules.map(projectSchedule -> {
            List<ProjectReply> replies = projectSchedule.getReplies()
                    .stream().filter(projectReply -> projectReply.getReplyOption() == SCHEDULE && projectReply.getReplyState() == USABLE).collect(Collectors.toList());
            log.info("sdfsfsdfsfsfsf : {}", projectSchedule.getProjectManagerList());
            List<ProjectManagersSchedule> managers = projectSchedule.getProjectManagerList();
            log.info("afsdfa : {}", managers);
            return ProjectScheduleResponse.from(projectSchedule, replies, managers);
        });
    }

    /* 일정 댓글 작성 */
    public Long scheduleReply(ScheduleReplyCreateRequest replyRequest, Long scheduleCode, CustomUser customUser) {

        if (replyRequest.getReplyBody() == null || replyRequest.getReplyBody().isEmpty()) {
            throw new NotFoundException(NOT_FOUND_REPLY_BODY);
        }

        MemberInfo member = infoRepository.getReferenceById(customUser.getInfoCode());

        final ProjectReply newProjectReply = ProjectReply.of(
                scheduleCode,
                member,
                replyRequest.getReplyBody(),
                SCHEDULE
        );


        final ProjectReply projectReply = projectReplyRepository.save(newProjectReply);

        return projectReply.getReplyCode();
    }

    /* 댓글 수정 */
    public void replyUpdate(Long replyCode, SchReplyUpdate replyRequest) {

        ProjectReply reply = projectReplyRepository.findByReplyCodeAndReplyStateNot(replyCode, ProjectStatusType.DELETED)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_REPLY_CODE));

        if (replyRequest.getReplyBody() == null || replyRequest.getReplyBody().isEmpty()) {
            throw new NotFoundException(NOT_FOUND_REPLY_BODY);
        }

        reply.update(
                replyRequest.getReplyBody()
        );
    }

}

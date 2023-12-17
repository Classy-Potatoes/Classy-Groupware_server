package com.potatoes.cg.projectTodo.service;

import com.potatoes.cg.calendar.domain.type.StatusType;
import com.potatoes.cg.common.exception.NotFoundException;
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
import com.potatoes.cg.project.dto.response.MyTodoResponse;
import com.potatoes.cg.project.service.ProjectMemberService;
import com.potatoes.cg.project.service.ProjectService;
import com.potatoes.cg.projectManagers.domain.ProjectManagersSchedule;
import com.potatoes.cg.projectManagers.domain.ProjectManagersTodo;
import com.potatoes.cg.projectManagers.domain.repository.ProjectManagersTodoRepository;
import com.potatoes.cg.projectSchedule.dto.request.SchReplyUpdate;
import com.potatoes.cg.projectSchedule.dto.request.ScheduleReplyCreateRequest;
import com.potatoes.cg.projectTodo.domain.ProjectTodo;
import com.potatoes.cg.projectTodo.domain.repository.ProjectTodoRepository;
import com.potatoes.cg.projectTodo.dto.request.ProjectTodoCreateRequest;
import com.potatoes.cg.projectTodo.dto.request.ProjectTodoUpdateRequest;
import com.potatoes.cg.projectTodo.dto.response.ProjectTodoListResponse;
import com.potatoes.cg.projectTodo.dto.response.ProjectTodoResponse;
import com.potatoes.cg.projectTodo.presentation.TodoListResponse;
import com.potatoes.cg.projectTodolist.domain.ProjectTodolist;
import com.potatoes.cg.projectTodolist.domain.repository.ProjectTodolistRepository;
import com.potatoes.cg.projectTodolist.dto.request.ProjectTodolistCreateRequest;
import com.potatoes.cg.projectTodolist.dto.request.ProjectTodolistUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.potatoes.cg.calendar.domain.type.StatusType.*;
import static com.potatoes.cg.common.exception.type.ExceptionCode.*;
import static com.potatoes.cg.project.domain.type.ProjectOptionType.SCHEDULE;
import static com.potatoes.cg.project.domain.type.ProjectOptionType.TODO;
import static com.potatoes.cg.project.domain.type.ProjectStatusType.USABLE;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProjectTodoService {

    private final MemberRepository memberRepository;
    private final ProjectService projectService;
    private final ProjectTodoRepository projectScheduleRepository;
    private final ProjectParticipantRepository projectParticipantRepository;
    private final ProjectMemberService projectMemberService;
    private final ProjectManagersTodoRepository projectManagerRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTodoRepository projectTodoRepository;
    private final ProjectTodolistRepository projectTodolistRepository;
    private final InfoRepository infoRepository;
    private final ProjectReplyRepository projectReplyRepository;

    /* 할일 등록 */
    public void save(Long projectCode, ProjectTodoCreateRequest todoRequest, CustomUser customUser) {
        log.info("customeuser : {}", customUser.getMemberCode());
        log.info("rerrrr : {}", memberRepository.findByMemberInfoInfoCode(todoRequest.getProjectTodolistCreateRequestList().get(0).getAttendant()));

        List<ProjectTodolist> projectTodolists = todoRequest.getProjectTodolistCreateRequestList().stream().map(
                todoReq -> ProjectTodolist.of(
                        todoReq.getTodoBody(),
                        todoReq.getEndDates(),
                        ProjectManagersTodo.of(memberRepository.findByMemberInfoInfoCode(todoReq.getAttendant()
                                )
                        )
                )).collect(Collectors.toList());

        log.info("memeeeeee {}", projectTodolists);
        for (int i = 0; i < projectTodolists.size(); i++) {
            LocalDate setEndDate = projectTodolists.get(i).getTodoEndDate();
            if (setEndDate == null) {
                throw new NotFoundException(NOT_FOUND_VALID_DATE);
            }
        }

        if (todoRequest.getTodoTitle() == null || todoRequest.getTodoTitle().isEmpty()) {
            throw new NotFoundException(NOT_FOUND_VALID_TITLE);
        }


        final Project project = projectRepository.getReferenceById(projectCode);

        final Member member = memberRepository.getReferenceById(customUser.getMemberCode());

        log.info("memberrr : {}", member);
        final ProjectTodo newProjectTodo = ProjectTodo.of(
                todoRequest.getTodoTitle(),
                member,
                project,
                projectTodolists
        );

        log.info("sssssssssss {}", newProjectTodo);
        ProjectTodo projectTodo = projectTodoRepository.save(newProjectTodo);
        log.info("bbbbbbbbbbbbbbb {}", projectTodo);
//        log.info("adfsfsd : {}", projectTodolists.get(0).getProjectManager());
        for (int i = 0; i < projectTodolists.size(); i++) {
            projectTodolists.get(i).getProjectManager().setTodoList(projectTodolists.get(i));
        }

    }
//        List<Long> todoListCode = projectTodo.getProjectTodolist()
//                .stream().map(code -> code.getTodoListCode()).collect(Collectors.toList());
//
//        List<Long> managerCode = projectTodo.getProjectTodolist()
//                .stream().map(a -> a.getProjectManager().getProjectManagerCode()).collect(Collectors.toList());
//
//        log.info("todoListCode : {}", todoListCode);
//        log.info("managerCode : {}", managerCode);
//        for (int i = 0; i < todoListCode.size(); i++) {
//            ProjectManagersTodo managerTodo = projectManagerRepository.findById(managerCode.get(i)).orElseThrow();
//            managerTodo.setTodoListCode(todoListCode.get(i));
//        }

    /* 할일글 수정 */
    public void update(Long projectCode, Long todoCode, ProjectTodoUpdateRequest todoRequest) {

        ProjectTodo projectTodo = projectTodoRepository.findByTodoCode(todoCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_TODO_CODE));

        List<Long> todoListCode = projectTodo.getProjectTodolist().stream().map(
                todo -> todo.getTodoListCode()
        ).collect(Collectors.toList());

        projectTodolistRepository.deleteAllByTodoCode(todoCode);
        projectManagerRepository.deleteAllByTodoListTodoListCodeIn(todoListCode);

        List<ProjectTodolist> newProjectTodolists = todoRequest.getProjectTodolistUpdateRequestList().stream().map(
                todoReq -> ProjectTodolist.of(
                        todoReq.getTodoBody(),
                        todoReq.getEndDates(),
                        ProjectManagersTodo.of(memberRepository.findByMemberInfoInfoCode(todoReq.getAttendant()
                                )
                        )
                )).collect(Collectors.toList());

        for (int i = 0; i < newProjectTodolists.size(); i++) {
            LocalDate setEndDate = newProjectTodolists.get(i).getTodoEndDate();
            if (setEndDate == null) {
                throw new NotFoundException(NOT_FOUND_VALID_DATE);
            }
        }

        if (todoRequest.getTodoTitle() == null || todoRequest.getTodoTitle().isEmpty()) {
            throw new NotFoundException(NOT_FOUND_VALID_TITLE);
        }

        final Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROJECT_CODE));

        projectTodo.update(
                todoRequest.getTodoTitle(),
                project,
                newProjectTodolists
        );

//        log.info("asdfsfsdfsd : {}", newProjectTodolists.get(0).getProjectManager());
        for (int i = 0; i < newProjectTodolists.size(); i++) {
            newProjectTodolists.get(i).getProjectManager().setTodoList(newProjectTodolists.get(i));
        }
    }

    /* 할일글 삭제 */
    public void delete(Long projectCode, Long todoCode) {

        ProjectTodo projectTodo = projectTodoRepository.findByTodoCode(todoCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_TODO_CODE));

        List<Long> todoListCode = projectTodo.getProjectTodolist().stream().map(
                todo -> todo.getTodoListCode()
        ).collect(Collectors.toList());

        projectTodoRepository.deleteById(todoCode);
        projectTodolistRepository.deleteAllByTodoCode(todoCode);
        projectManagerRepository.deleteAllByTodoListTodoListCodeIn(todoListCode);
    }

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 10, Sort.by("todoCode").descending());
    }

    /* 할일글 조회 */
    @Transactional(readOnly = true)
    public Page<ProjectTodoResponse> getTodo(Integer page, Long projectCode, CustomUser customUser) {

        Page<ProjectTodo> projectTodos = projectTodoRepository.findByProjectProjectCodeAndTodoStatusNotAndMemberMemberCode(projectCode, getPageable(page), DELETED, customUser.getMemberCode());

        return projectTodos.map(projectTodo -> {

            List<ProjectTodoListResponse> projectTodolists = projectTodo.getProjectTodolist()
                    .stream().map(projectTodolist -> {
//                        log.info("sdfsefhseuif : {}", projectTodolist);
                        ;
//                        log.info("abcdeddd : {}", projectTodolist.getProjectManager());
                        return ProjectTodoListResponse.from(projectTodolist);
                    }).collect(Collectors.toList());

//            log.info("sdfsdfsdf : {}", projectTodo.getMember().getMemberCode());
            List<ProjectReply> replies = projectTodo.getReplies()
                    .stream().filter(projectReply -> projectReply.getReplyOption() == TODO && projectReply.getReplyState() == USABLE).collect(Collectors.toList());
            return ProjectTodoResponse.from(projectTodo, projectTodolists, replies);
        });
    }

    /* 완료 여부 */
    public void checked(Long projectCode, Long todoCode, Long todoListCode, CustomUser customUser) {
        ProjectTodolist projectTodolist = projectTodolistRepository.findById(todoListCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_TODO_CODE));

        StatusType curStatus = projectTodolist.getTofoStatus();
        log.info("aaaaaa : {}", curStatus);

        StatusType newStatus = (curStatus == UNFINISHED) ? FINISHED : UNFINISHED;
        log.info("bbbbb : {}", newStatus);
        projectTodolist.checked(
                newStatus
        );
    }

    /* 내 할일리스트 조회 */
    @Transactional(readOnly = true)
    public List<TodoListResponse> getTodoList(CustomUser customUser) {
        log.info("sfsdfsdfsf : {}", customUser);
        List<ProjectTodolist> todoListResponses = projectTodolistRepository.findAllByTofoStatusAndProjectManagerMemberMemberCodeOrderByTodoListCodeDesc(UNFINISHED, customUser.getMemberCode());

        log.info("sfsdfsdf : {}", todoListResponses);
        List<TodoListResponse> projectTodolists = todoListResponses.stream().map(
                todoList -> TodoListResponse.from(
                        todoList.getTodoListCode(),
                        todoList.getTodoBody(),
                        todoList.getTodoEndDate()
                )
        ).collect(Collectors.toList());

        return projectTodolists;
    }

    /* 할일 댓글 등록 */
    public Long todoReply(ScheduleReplyCreateRequest replyRequest, Long todoCode, CustomUser customUser) {

        if (replyRequest.getReplyBody() == null || replyRequest.getReplyBody().isEmpty()) {
            throw new NotFoundException(NOT_FOUND_REPLY_BODY);
        }

        MemberInfo member = infoRepository.getReferenceById(customUser.getInfoCode());

        final ProjectReply newProjectReply = ProjectReply.of(
                todoCode,
                member,
                replyRequest.getReplyBody(),
                TODO
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

    /* 내 할일리스트 조회 {프로젝트 상관 x}*/
    public List<TodoListResponse> getAllTodoList(CustomUser customUser) {

        log.info("sfsdfsdfsf : {}", customUser);
        List<ProjectTodolist> todoListResponses = projectTodolistRepository.findAllByTofoStatusAndProjectManagerMemberMemberCodeOrderByTodoListCodeDesc(UNFINISHED, customUser.getMemberCode());

        log.info("sfsdfsdf : {}", todoListResponses);
        List<TodoListResponse> projectTodolists = todoListResponses.stream().map(
                todoList -> TodoListResponse.from(
                        todoList.getTodoListCode(),
                        todoList.getTodoBody(),
                        todoList.getTodoEndDate()
                )
        ).collect(Collectors.toList());

        return projectTodolists;
    }

    /* 내 할일 조회 */
    @Transactional(readOnly = true)
    public Page<MyTodoResponse> getMyTodo(final Integer page, final CustomUser customUser) {

        Page<ProjectTodolist> todos = projectTodolistRepository.findByMyTodo(getPageable(page), customUser.getMemberCode());

        return todos.map(projectTodolist -> MyTodoResponse.from(projectTodolist));
    }
}

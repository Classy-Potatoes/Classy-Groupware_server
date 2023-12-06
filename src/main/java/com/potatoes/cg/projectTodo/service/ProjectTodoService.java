package com.potatoes.cg.projectTodo.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.member.domain.repository.MemberRepository;
import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.repository.ProjectParticipantRepository;
import com.potatoes.cg.project.domain.repository.ProjectRepository;
import com.potatoes.cg.project.service.ProjectMemberService;
import com.potatoes.cg.project.service.ProjectService;
import com.potatoes.cg.projectManagers.domain.ProjectManagers;
import com.potatoes.cg.projectManagers.domain.repository.ProjectManagersRepository;
import com.potatoes.cg.projectTodo.domain.ProjectTodo;
import com.potatoes.cg.projectTodo.domain.repository.ProjectTodoRepository;
import com.potatoes.cg.projectTodo.dto.request.ProjectTodoCreateRequest;
import com.potatoes.cg.projectTodo.dto.request.ProjectTodoUpdateRequest;
import com.potatoes.cg.projectTodolist.domain.ProjectTodolist;
import com.potatoes.cg.projectTodolist.domain.repository.ProjectTodolistRepository;
import com.potatoes.cg.projectTodolist.dto.request.ProjectTodolistCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.potatoes.cg.calendar.domain.type.StatusType.DELETED;
import static com.potatoes.cg.common.exception.type.ExceptionCode.*;

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
    private final ProjectManagersRepository projectManagerRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTodoRepository projectTodoRepository;
    private final ProjectTodolistRepository projectTodolistRepository;

    /* 할일 등록 */
    public void save(Long projectCode, ProjectTodoCreateRequest todoRequest, int memberCode) {

        List<ProjectTodolistCreateRequest> todoReqList = todoRequest.getProjectTodolistCreateRequestList();
        List<ProjectTodolist> projectTodolists = new ArrayList<>();
        for (int i = 0; i < todoReqList.size(); i++) {

            final ProjectTodolistCreateRequest todoListReq = todoReqList.get(i);

            ProjectManagers projectManager = memberRepository.findById(todoListReq.getAttendant())
                    .map(member -> ProjectManagers.of(member))
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBERCODE));

            log.info("adsfsdkfsdjwejoweoiweo : {}", projectManager);
            final ProjectTodolist newProjectTodolist = ProjectTodolist.of(
                    todoListReq.getTodoBody(),
                    todoListReq.getEndDates(),
                    projectManager
            );
            projectTodolists.add(newProjectTodolist);

        }

        final Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROJECT_CODE));

        final ProjectTodo newProjectTodo = ProjectTodo.of(
                todoRequest.getTodoTitle(),
                project,
                projectTodolists
        );


        log.info("123213213 : {}", todoReqList);

        projectTodoRepository.save(newProjectTodo);

    }

    /* 할일글 수정 */
    public void update(Long projectCode, Long todoCode, ProjectTodoUpdateRequest todoRequest) {

        ProjectTodo projectTodo = projectTodoRepository.findByTodoCodeAndTodoStatusNot(todoCode, DELETED)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_TODO_CODE));

        projectTodolistRepository.deleteAll(projectTodo.getProjectTodolist());

        ProjectTodolist projectTodolist = projectTodolistRepository.findById(projectTodo.getTodoCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_TODO_CODE));

        projectManagerRepository.delete(projectTodolist.getProjectManager());

        List<ProjectTodolistCreateRequest> todoReqList = todoRequest.getProjectTodolistCreateRequestList();
        List<ProjectTodolist> projectTodolists = new ArrayList<>();
        for (int i = 0; i < todoReqList.size(); i++) {

            final ProjectTodolistCreateRequest todoListReq = todoReqList.get(i);

            ProjectManagers projectManager = memberRepository.findById(todoListReq.getAttendant())
                    .map(member -> ProjectManagers.of(member))
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBERCODE));

            projectTodolist.update(
                    todoListReq.getTodoBody(),
                    todoListReq.getEndDates(),
                    projectManager
            );
            projectTodolists.add(projectTodolist);
        }

        final Project project = projectRepository.findById(projectCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROJECT_CODE));

        projectTodo.update(
                todoRequest.getTodoTitle(),
                project,
                projectTodolists
        );

    }

    /* 할일글 삭제 */
    public void delete(Long projectCode, Long todoCode) {

        ProjectTodo projectTodo = projectTodoRepository.findByTodoCode(todoCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_TODO_CODE));

        List<Long> todoListCode = projectTodo.getProjectTodolist().stream().map(
                todo -> todo.getTodoCode()
        ).collect(Collectors.toList());

        log.info("abcefdfdsfs :{}", todoListCode);
//        List<ProjectTodolist> projectTodolists = projectTodoRepository.findAllByTodoCodeIn(todoListCode);

        projectTodoRepository.deleteById(todoCode);
        projectTodolistRepository.deleteAll(projectTodo.getProjectTodolist());
    }
}
package com.potatoes.cg.project.service;


import com.potatoes.cg.common.util.MultipleFileUploadUtils;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.project.domain.*;
import com.potatoes.cg.project.domain.repository.ProjectManagerRepository;

import com.potatoes.cg.project.domain.repository.ProjectRepository;
import com.potatoes.cg.project.domain.repository.ProjectTaskRepository;
import com.potatoes.cg.project.domain.type.ProjectFileType;
import com.potatoes.cg.project.dto.request.ProjectTaskCreateRequest;
import com.potatoes.cg.project.dto.request.TaskUpdateRequest;
import com.potatoes.cg.project.dto.response.MyTaskResponse;
import com.potatoes.cg.project.dto.response.ProjectPostResponse;
import com.potatoes.cg.project.dto.response.ProjectTaskResponse;
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

import static com.potatoes.cg.project.domain.type.ProjectStatusType.USABLE;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TaskService {

    private final ProjectRepository projectRepository;
    private final InfoRepository infoRepository;
    private final ProjectManagerRepository projectManagerRepository;
    private final ProjectTaskRepository projectTaskRepository;

    @Value("${file.projecttask-dir}")
    private String PROJECTTASK_DIR;
    @Value("${file.projecttask-url}")
    private String PROJECTTASK_URL;

    private String getRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    /* 업무 글 작성 */
    public Long taskSave(final ProjectTaskCreateRequest projecttaskRequest, final CustomUser customUser,
                         final List<MultipartFile> attachment) {

        List<ProjectFile> files = new ArrayList<>();

        // 파일이 제공되었는지 확인
        if (attachment != null && !attachment.isEmpty()) {
            /* 전달된 파일을 서버의 지정 경로에 저장 */
            List<String> replaceFileNames = MultipleFileUploadUtils.saveFiles(PROJECTTASK_DIR, attachment);

            /* 파일 정보 저장 */
            for (String replaceFileName : replaceFileNames) {
                ProjectFile fileEntity = new ProjectFile(
                        replaceFileName,
                        PROJECTTASK_URL + replaceFileName,
                        getRandomName(),
                        getFileExtension(replaceFileName),
                        ProjectFileType.TASK
                );
                files.add(fileEntity);
            }
        }

        MemberInfo member = infoRepository.getReferenceById(customUser.getInfoCode());

        /* 업무 담당자 저장 */
        List<ProjectManager> managers = new ArrayList<>();
        for (MemberInfo infoCode : projecttaskRequest.getProjectManagers() ) {
            ProjectManager managerEntity = new ProjectManager(infoCode);
            managers.add(managerEntity);
        }


        final ProjectTask newProjectTask = ProjectTask.of(
                projecttaskRequest.getProjectCode(),
                member,
                projecttaskRequest.getTaskTitle(),
                projecttaskRequest.getTaskBody(),
                projecttaskRequest.getTaskStartDate(),
                projecttaskRequest.getTaskEndDate(),
                projecttaskRequest.getTaskPriority(),
                files,
                managers
        );


        final ProjectTask projectTask = projectTaskRepository.save(newProjectTask);

        return projectTask.getTaskCode();
    }

    /* 프로젝트 업무 삭제 */
    public void taskDelete(final Long taskCode)  {
        projectTaskRepository.deleteById(taskCode);
    }

    /* 프로젝트 업무 수정 */
    public void taskUpdate(final Long taskCode, final TaskUpdateRequest taskUpdateRequest) {

        ProjectTask task = projectTaskRepository.getReferenceById(taskCode);

        task.update(
                taskUpdateRequest.getTaskTitle(),
                taskUpdateRequest.getTaskBody(),
                taskUpdateRequest.getTaskStartDate(),
                taskUpdateRequest.getTaskEndDate(),
                taskUpdateRequest.getTaskPriority()
        );
    }

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("taskRequestDate").descending());
    }

    /* 내 업무 조회 */
    @Transactional(readOnly = true)
    public Page<MyTaskResponse> getMyTask(final Integer page, final CustomUser customUser) {

        Page<ProjectTask> tasks = projectTaskRepository.findByMyTask(getPageable(page), customUser.getInfoCode());

        return tasks.map(projectTask -> MyTaskResponse.from(projectTask));
    }

    /* 업무 조회 */
    @Transactional(readOnly = true)
    public Page<ProjectTaskResponse> getTaskDetail(final Integer page, final Long projectCode, final CustomUser customUser) {

        Page<ProjectTask> tasks = projectTaskRepository.findByProjectCodeAndTaskDeleteStatus(projectCode, getPageable(page),  USABLE );

        return tasks.map(task -> {
            List<ProjectReply> replies = task.getReplies(); // 댓글을 즉시 가져오기
            List<ProjectManager> managers = task.getProjectManagers();
            List<ProjectFile> files = task.getFileEntity();
            return ProjectTaskResponse.from(task, replies, managers, files, customUser);
        });
    }

}

package com.potatoes.cg.project.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.common.util.MultipleFileUploadUtils;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.project.domain.*;
import com.potatoes.cg.project.domain.repository.ProjectManagerRepository;
import com.potatoes.cg.project.domain.repository.ProjectParticipantRepository;
import com.potatoes.cg.project.domain.repository.ProjectRepository;
import com.potatoes.cg.project.domain.repository.ProjectTaskRepository;
import com.potatoes.cg.project.dto.request.ProjectTaskCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.potatoes.cg.common.exception.type.ExceptionCode.NOT_FOUND_INFO_CODE;
import static com.potatoes.cg.common.exception.type.ExceptionCode.NOT_PROJECT_CODE;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final ProjectRepository projectRepository;
    private final InfoRepository infoRepository;
    private final ProjectManagerRepository projectManagerRepository;
    private final ProjectTaskRepository projectTaskRepository;

    @Value("${file.projecttask-dir}")
    private String PROJECTTASK_DIR;

    private String getRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    /* 업무 글 작성 */
    public Long taskSave(final ProjectTaskCreateRequest projecttaskRequest, final CustomUser customUser,
                         final List<MultipartFile> attachment, final List<ProjectManager> projectManagers) {

        /* 전달 된 파일을 서버의 지정 경로에 저장 */
        List<String> replaceFileNames = MultipleFileUploadUtils.saveFiles(PROJECTTASK_DIR, (attachment));

        /*파일 정보 저장 */
        List<TaskFile> files = new ArrayList<>();
        for (String replaceFileName : replaceFileNames) {
            TaskFile fileEntity = new TaskFile(
                    replaceFileName,
                    PROJECTTASK_DIR,
                    getRandomName(),
                    getFileExtension(replaceFileName)

            );
            files.add(fileEntity);
        }

        Project project = projectRepository.findById(projecttaskRequest.getProjectCode())
                .orElseThrow(() -> new NotFoundException(NOT_PROJECT_CODE));

        MemberInfo member = infoRepository.findById(customUser.getInfoCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_INFO_CODE));

        /* 업무 담당자 저장 */
        List<ProjectManager> managers = new ArrayList<>();
        for (ProjectManager manager : projectManagers) {
            ProjectManager managerEntity = new ProjectManager(
                    member.getInfoCode()
            );
            managers.add(managerEntity);
        }

        final ProjectTask newProjectTask = ProjectTask.of(
                project,
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
}

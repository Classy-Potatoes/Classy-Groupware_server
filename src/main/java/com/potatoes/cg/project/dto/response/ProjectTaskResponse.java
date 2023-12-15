package com.potatoes.cg.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.project.domain.ProjectFile;
import com.potatoes.cg.project.domain.ProjectManager;
import com.potatoes.cg.project.domain.ProjectReply;
import com.potatoes.cg.project.domain.ProjectTask;
import com.potatoes.cg.project.domain.type.ProjectStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class ProjectTaskResponse {

    private final Long taskCode;
    private final String taskTitle;
    private final String taskBody;
    private final String taskStatus;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final Date taskStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final Date taskEndDate;
    private final String taskPriority;
    private final String memberName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime taskRequestDate;
    private final ProjectStatusType taskDeleteStatus;
    private final Long memberCode;
    private final Long projectCode;

    private final List<Map<String, Object>> managers;

    private final List<Map<String, Object>> replies;

    private final CustomUser customUser;

    private final List<Map<String, Object>>  files;



    public static ProjectTaskResponse from(ProjectTask task, List<ProjectReply> replies, List<ProjectManager> managers, List<ProjectFile> files, CustomUser customUser) {

        List<Map<String, Object>> repliesMap = replies.stream()
                .filter(reply -> !ProjectStatusType.DELETED.equals(reply.getReplyState())) // 삭제된 댓글 제외
                .map(reply -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("memberName", reply.getMember().getInfoName());
                    map.put("replyBody", reply.getReplyBody());
                    map.put("replyCreatedDate", reply.getReplyCreatedDate());
                    map.put("memberCode", reply.getMember().getInfoCode());
                    map.put("replyCode", reply.getReplyCode());
                    return map;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> managersMap = managers.stream().map(manager -> {
            Map<String, Object> map = new HashMap<>();
            map.put("memberName", manager.getMemberCode().getInfoName());
            map.put("memberCode", manager.getMemberCode().getInfoCode());
          return map;
        }).collect(Collectors.toList());

        // 첨부파일
        List<Map<String, Object>> filesMap = files.stream().map(file -> {
            Map<String, Object> map = new HashMap<>();
            map.put("fileName", file.getFileName());
            map.put("filePathName", file.getFilePathName());
            return map;
        }).collect(Collectors.toList());

        return new ProjectTaskResponse(
                task.getTaskCode(),
                task.getTaskTitle(),
                task.getTaskBody(),
                task.getTaskStatus(),
                task.getTaskStartDate(),
                task.getTaskEndDate(),
                task.getTaskPriority(),
                task.getMember().getInfoName(),
                task.getTaskRequestDate(),
                task.getTaskDeleteStatus(),
                task.getMember().getInfoCode(),
                task.getProjectCode(),
                managersMap,
                repliesMap,
                customUser,
                filesMap
        );
    }
}

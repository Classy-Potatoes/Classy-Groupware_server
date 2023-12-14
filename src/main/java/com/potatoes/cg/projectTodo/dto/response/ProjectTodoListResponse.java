package com.potatoes.cg.projectTodo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.project.domain.ProjectReply;
import com.potatoes.cg.projectTodolist.domain.ProjectTodolist;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class ProjectTodoListResponse {

    private final Long todoListCode;
    private final String todoListBody;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate todoListEndDate;

    private final String managerName;

    private final List<Map<String, Object>> replies;

    public static ProjectTodoListResponse from(ProjectTodolist projectTodolist, List<ProjectReply> replies) {

        List<Map<String, Object>> repliesMap = replies.stream().map(reply -> {
            Map<String, Object> map = new HashMap<>();
            map.put("memberName", reply.getMember().getInfoName());
            map.put("replyBody", reply.getReplyBody());
            map.put("replyCreatedDate", reply.getReplyCreatedDate());
            return map;
        }).collect(Collectors.toList());

        return new ProjectTodoListResponse(
                projectTodolist.getTodoListCode(),
                projectTodolist.getTodoBody(),
                projectTodolist.getTodoEndDate(),
                projectTodolist.getProjectManager().getMember().getMemberInfo().getInfoName(),
                repliesMap
        );
    }

}

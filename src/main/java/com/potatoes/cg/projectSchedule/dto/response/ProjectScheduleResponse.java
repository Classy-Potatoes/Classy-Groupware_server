package com.potatoes.cg.projectSchedule.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.project.domain.ProjectReply;
import com.potatoes.cg.projectManagers.domain.ProjectManagersSchedule;
import com.potatoes.cg.projectSchedule.domain.ProjectSchedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class ProjectScheduleResponse {

    private final Long scheduleCode;
    private final String scheduleTitle;
    private final String scheduleBody;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private final LocalDateTime scheduleStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private final LocalDateTime scheduleEndDate;
    private final String memberName;
    private final Long projectCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private final LocalDateTime modifiedDate;

    private final List<Map<String, Object>> managers;

    private final List<Map<String, Object>> replies;

    private final String memberId;
    private final Long infoCode;

    public static ProjectScheduleResponse from(ProjectSchedule projectSchedule, List<ProjectReply> replies, List<ProjectManagersSchedule> managers, CustomUser customUser) {

        List<Map<String, Object>> repliesMap = replies.stream().map(reply -> {
            Map<String, Object> map = new HashMap<>();
            map.put("memberName", reply.getMember().getInfoName());
            map.put("replyBody", reply.getReplyBody());
            map.put("replyCreatedDate", reply.getReplyCreatedDate());
            map.put("replyCode", reply.getReplyCode());
            map.put("replyModifyDate", reply.getReplyModifyDate());
            map.put("infoCode", reply.getMember().getInfoCode());
            return map;
        }).collect(Collectors.toList());

        List<Map<String, Object>> managersMap = managers.stream().map(manager -> {
            Map<String, Object> map = new HashMap<>();
            map.put("memberName", manager.getMember().getMemberInfo().getInfoName());
            map.put("infoCode", manager.getMember().getMemberInfo().getInfoCode());
            return map;
        }).collect(Collectors.toList());

        return new ProjectScheduleResponse(
                projectSchedule.getScheduleCode(),
                projectSchedule.getScheduleTitle(),
                projectSchedule.getScheduleBody(),
                projectSchedule.getScheduleStartDate(),
                projectSchedule.getScheduleEndDate(),
                projectSchedule.getMember().getMemberInfo().getInfoName(),
                projectSchedule.getProject().getProjectCode(),
                projectSchedule.getScheduleModifyDate(),
                managersMap,
                repliesMap,
                projectSchedule.getMember().getMemberId(),
                customUser.getInfoCode()
        );
    }

}

package com.potatoes.cg.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.project.domain.ProjectPost;
import com.potatoes.cg.project.domain.ProjectReply;
import com.potatoes.cg.project.domain.type.ProjectStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class ProjectPostResponse {

    private final Long postCode;
    private final String postTitle;
    private final String postBody;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime postCreatedDate;
    private final ProjectStatusType postStatus;
//    private final Long memberCode;
    private final String memberName;
    private final Long projectCode;

    private final List<Map<String, Object>> replies;

    public static ProjectPostResponse from(ProjectPost post, List<ProjectReply> replies) {

        List<Map<String, Object>> repliesMap = replies.stream().map(reply -> {
            Map<String, Object> map = new HashMap<>();
            map.put("memberName", reply.getMember().getInfoName());
            map.put("replyBody", reply.getReplyBody());
            map.put("replyCreatedDate", reply.getReplyCreatedDate());
            return map;
        }).collect(Collectors.toList());

        return new ProjectPostResponse(
                post.getPostCode(),
                post.getPostTitle(),
                post.getPostBody(),
                post.getPostCreatedDate(),
                post.getPostStatus(),
                post.getMember().getInfoName(),
                post.getProjectCode(),
                repliesMap
        );
    }
}

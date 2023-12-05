package com.potatoes.cg.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.project.domain.ProjectReply;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PostReplyResponse {

    private final Long replyCode;
    private final String replyBody;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime replyCreatedDate;
    private final Long postCode;
    private final String memberName;

    public static PostReplyResponse from(ProjectReply reply) {
        return new PostReplyResponse(
                reply.getReplyCode(),
                reply.getReplyBody(),
                reply.getReplyCreatedDate(),
                reply.getPostCode(),
                reply.getMember().getInfoName()
        );
    }
}

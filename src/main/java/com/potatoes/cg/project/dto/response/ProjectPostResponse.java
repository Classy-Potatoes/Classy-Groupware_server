package com.potatoes.cg.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.project.domain.ProjectPost;
import com.potatoes.cg.project.domain.type.ProjectStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

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

    public static ProjectPostResponse from(ProjectPost post) {
        return new ProjectPostResponse(
                post.getPostCode(),
                post.getPostTitle(),
                post.getPostBody(),
                post.getPostCreatedDate(),
                post.getPostStatus(),
                post.getMember().getInfoName(),
                post.getProject().getProjectCode()
        );
    }
}

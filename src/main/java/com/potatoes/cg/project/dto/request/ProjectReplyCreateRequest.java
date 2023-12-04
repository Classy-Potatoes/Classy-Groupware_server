package com.potatoes.cg.project.dto.request;

import com.potatoes.cg.project.domain.ProjectPost;
import com.potatoes.cg.project.domain.type.ReplyOptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class ProjectReplyCreateRequest {

    @NotBlank
    private final String replyBody;

    @Min(value = 1)
    private final Long postCode;

}

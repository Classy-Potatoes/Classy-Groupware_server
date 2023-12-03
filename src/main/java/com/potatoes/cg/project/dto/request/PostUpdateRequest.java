package com.potatoes.cg.project.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class PostUpdateRequest {

    @NotBlank
    private final String postTitle;

    @NotBlank
    private final String postBody;

    @Min(value = 1)
    private final Long projectCode;

}

package com.potatoes.cg.project.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class ProjectPostCreateRequest {

    @NotBlank
    private final String postTitle;

    @NotBlank
    private final String postBody;

    @Min(value = 1)
    private final Long projectCode;
}

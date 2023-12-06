package com.potatoes.cg.projectSchedule.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class ProjectManagerCreateRequest {

    @NotBlank
    private final Long membercode;

    private final String scheduleCode;
}

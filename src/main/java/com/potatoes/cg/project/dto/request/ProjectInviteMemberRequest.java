package com.potatoes.cg.project.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;

@RequiredArgsConstructor
@Getter
public class ProjectInviteMemberRequest {

    @Min(value = 1)
    private final Long projectCode;
    @Min(value = 1)
    private final Long memberCode;
}

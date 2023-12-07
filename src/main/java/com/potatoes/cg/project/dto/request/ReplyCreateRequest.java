package com.potatoes.cg.project.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class ReplyCreateRequest {

    @NotBlank
    private final String replyBody;

    @Min(value = 1)
    private final Long postCode;

    @Min(value = 1)
    private  final Long taskCode;

    @Min(value = 1)
    private  final Long scheduleCode;

    @Min(value = 1)
    private  final Long todoListCode;


}

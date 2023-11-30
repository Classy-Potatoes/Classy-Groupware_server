package com.potatoes.cg.note.dto.request;

import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class NoteMoveRequest {

    @Min(value = 1)
    private final Long noteCode;

    @NotBlank
    private final Member noteSender;

    @NotBlank
    private final Member noteReceiver;

    @NotBlank
    private final String noteBody;

}
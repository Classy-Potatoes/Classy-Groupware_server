package com.potatoes.cg.note.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Getter
@ToString
public class NoteReplyCreateRequest {

    @NotNull
    private final String noteReceiver;

    @NotBlank
    private final String noteBody;

}
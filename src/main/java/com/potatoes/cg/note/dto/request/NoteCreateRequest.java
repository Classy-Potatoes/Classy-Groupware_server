package com.potatoes.cg.note.dto.request;

import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class NoteCreateRequest {

    @Min(value = 1)
    private final Long noteCode;
//
//    @NotBlank
//    private final Member noteSender;

    @NotBlank
    private final Member noteReceiver;

    private final LocalDate noteSentDate;

    @NotBlank
    private final String noteBody;

}
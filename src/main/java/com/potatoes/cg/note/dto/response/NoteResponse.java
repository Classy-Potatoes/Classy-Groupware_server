package com.potatoes.cg.note.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.note.domain.Note;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class NoteResponse {

    private final Long noteCode;

    private final String deptName;

    private final String jobName;

    private final String noteSender;

    private final String noteReceiver;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime noteSentDate;

    private final String noteBody;

    public static NoteResponse from(Note note) {

        return new NoteResponse(
                note.getNoteCode(),
                note.getNoteSender().getMemberInfo().getDept().getDeptName(), note.getNoteSender().getMemberInfo().getJob().getJobName(),
                note.getNoteSender().getMemberInfo().getInfoName(), note.getNoteReceiver().getMemberInfo().getInfoName(),
                note.getNoteSentDate(),
                note.getNoteBody()
        );

    }

}
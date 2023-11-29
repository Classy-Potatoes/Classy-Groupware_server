package com.potatoes.cg.note.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.note.domain.Note;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class NoteResponse {

    private final String deptName;

    private final String jobName;

    private final String noteSender;

    private final String noteReceiver;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime noteSentDate;

    private final String noteBody;

    public static NoteResponse from(Note note) {

        return new NoteResponse(
                note.getNoteSender().getMemberInfo().getDeptCode().getDeptName(),
                note.getNoteSender().getMemberInfo().getJobCode().getJobName(),
                note.getNoteSender().getMemberInfo().getInfoName(),
                note.getNoteReceiver().getMemberInfo().getInfoName(),
                note.getNoteSentDate(),
                note.getNoteBody()
        );

    }

}
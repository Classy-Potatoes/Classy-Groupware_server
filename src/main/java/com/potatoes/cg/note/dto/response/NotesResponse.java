package com.potatoes.cg.note.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.note.domain.Note;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class NotesResponse {

    private final Long noteCode;

    private final String noteSender;

    private final String noteReceiver;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime noteSentDate;

    private final String noteBody;

    public static NotesResponse from(Note note) {

        return new NotesResponse(
                note.getNoteCode(),
                note.getNoteSender().getMemberInfoSelect().getInfoName(),
                note.getNoteReceiver().getMemberInfoSelect().getInfoName(),
                note.getNoteSentDate(),
                note.getNoteBody()
        );

    }

}
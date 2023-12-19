package com.potatoes.cg.note.dto.request;

import com.potatoes.cg.note.domain.type.NoteStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Getter
public class NoteMoveRequest {

    @NotNull
    private final String noteDivision; //쪽지 상태(받은 쪽지인지 보낸 쪽지인지)

    @NotNull
    private final NoteStatusType noteStatusType; //어떤 상태로 변화할 것인지(기본, 중요, 삭제)

}
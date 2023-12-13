package com.potatoes.cg.note.dto.request;

import com.potatoes.cg.note.domain.type.NoteStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class NoteMoveRequest {

    @NotNull
    private final List<Long> noteCodes; //바꾸고 싶은 쪽지 코드

    @NotNull
    private final String noteDivision; //쪽지 상태(받은 쪽지인지 보낸 쪽지인지)

    @NotNull
    private final NoteStatusType noteStatusType; //어떤 상태로 변화할 것인지(기본, 중요, 삭제)

}
package com.potatoes.cg.note.presentation;

import com.potatoes.cg.common.paging.Pagenation;
import com.potatoes.cg.common.paging.PagingButtonInfo;
import com.potatoes.cg.common.paging.PagingResponse;
import com.potatoes.cg.note.dto.response.NoteResponse;
import com.potatoes.cg.note.dto.response.NotesResponse;
import com.potatoes.cg.note.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/cg-api/v1/note/")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/send")
    public ResponseEntity<PagingResponse> getSentNotes(
            @RequestParam(defaultValue = "1") final Integer page,
//            @AuthenticationPrincipal Member memberCode
            @RequestParam final Long memberCode
    ) {
        LocalDateTime noteSentDate = LocalDateTime.now();

        final Page<NotesResponse> notes = noteService.sentNotes(page, memberCode, noteSentDate);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(notes);
        final PagingResponse pagingResponse = PagingResponse.of(notes.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);

    }

//    @GetMapping("/send/{note_no}")
//    public ResponseEntity<NoteResponse> getSentNote(@PathVariable final Long noteCode) {
//
//        final NoteResponse noteResponse = noteService.sentNote(noteCode);
//
//        return ResponseEntity.ok(noteResponse);
//
//    }

}
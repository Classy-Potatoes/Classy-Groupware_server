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

    //보낸 쪽지 전체 조회
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

    //보낸 쪽지 상세 조회
    @GetMapping("/send/{noteCode}")
    public ResponseEntity<NoteResponse> getSentNote(@PathVariable final Long noteCode) {

        final NoteResponse noteResponse = noteService.sentNote(noteCode);

        return ResponseEntity.ok(noteResponse);

    }

    //받은 쪽지 전체 조회
    @GetMapping("/received")
    public ResponseEntity<PagingResponse> getReceivedNotes(
            @RequestParam(defaultValue = "1") final Integer page,
//            @AuthenticationPrincipal Member memberCode
            @RequestParam final Long memberCode
    ) {

        LocalDateTime noteSentDate = LocalDateTime.now();

        final Page<NotesResponse> notes = noteService.receivedNotes(page, memberCode, noteSentDate);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(notes);
        final PagingResponse pagingResponse = PagingResponse.of(notes.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);

    }

    //받은 쪽지 상세 조회
    @GetMapping("/received/{noteCode}")
    public ResponseEntity<NoteResponse> getReceivedNote(@PathVariable final Long noteCode) {

        final NoteResponse noteResponse = noteService.receivedNote(noteCode);

        return ResponseEntity.ok(noteResponse);

    }

    //보낸 쪽지 삭제
    @DeleteMapping("/sent/{noteCode}")
    public ResponseEntity<Void> deleteSentNote(@PathVariable final Long noteCode) {

        noteService.deleteSentNote(noteCode);

        return ResponseEntity.noContent().build();

    }

    //받은 쪽지 삭제
    @DeleteMapping("/received/{noteCode}")
    public ResponseEntity<Void> deleteReceivedNote(@PathVariable final Long noteCode) {

        noteService.deleteReceivedNote(noteCode);

        return ResponseEntity.noContent().build();

    }


//    //쪽지 쓰기
//    @PostMapping("/write")
//    public ResponseEntity<Void> save(@RequestBody @Valid final NoteCreateRequest noteRequest, CustomUser customUser) {
//
//        final Long noteCode = noteService.save(noteRequest, customUser);
//
//        return ResponseEntity.created(URI.create("/api/v1/note/" + noteCode)).build();
//
//    }

}
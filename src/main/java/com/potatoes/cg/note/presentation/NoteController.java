package com.potatoes.cg.note.presentation;

import com.potatoes.cg.common.paging.Pagenation;
import com.potatoes.cg.common.paging.PagingButtonInfo;
import com.potatoes.cg.common.paging.PagingResponse;
import com.potatoes.cg.note.dto.request.NoteMoveRequest;
import com.potatoes.cg.note.dto.response.NoteResponse;
import com.potatoes.cg.note.dto.response.NotesResponse;
import com.potatoes.cg.note.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/cg-api/v1/note/")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    /********************************************* 보낸 쪽지함 *********************************************/

    //전체 조회
    @GetMapping("/sent")
    public ResponseEntity<PagingResponse> getSentNotes(
            @RequestParam(defaultValue = "1") final Integer page,
//            @AuthenticationPrincipal Member memberCode
            @RequestParam final Long memberCode
    ) {

        LocalDateTime noteSentDate = LocalDateTime.now();

        final Page<NotesResponse> notes = noteService.getSentNotes(page, memberCode, noteSentDate);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(notes);
        final PagingResponse pagingResponse = PagingResponse.of(notes.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);

    }

    //상세 조회
    @GetMapping("/sent/{noteCode}")
    public ResponseEntity<NoteResponse> getSentNote(@PathVariable final Long noteCode) {

        final NoteResponse noteResponse = noteService.getSentNote(noteCode);

        return ResponseEntity.ok(noteResponse);

    }

    //삭제
    @DeleteMapping("/sent/{noteCode}")
    public ResponseEntity<Void> deleteSentNote(@PathVariable final Long noteCode) {

        noteService.deleteSentNote(noteCode);

        return ResponseEntity.noContent().build();

    }

    //검색 조회
    @GetMapping("sent/search")
    public ResponseEntity<PagingResponse> getSentNoteBySearch(
            @RequestParam(defaultValue = "1") final Integer page, @RequestParam String searchCondition, @RequestParam String searchValue) {

        final Page<NotesResponse> notes = noteService.getSentNoteByNoteSenderOrNoteBody(page, searchCondition, searchValue);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(notes);
        final PagingResponse pagingResponse = PagingResponse.of(notes.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);

    }


    /********************************************* 받은 쪽지함 *********************************************/

    //전체 조회
    @GetMapping("/received")
    public ResponseEntity<PagingResponse> getReceivedNotes(
            @RequestParam(defaultValue = "1") final Integer page,
//            @AuthenticationPrincipal Member memberCode
            @RequestParam final Long memberCode
    ) {

        LocalDateTime noteSentDate = LocalDateTime.now();

        final Page<NotesResponse> notes = noteService.getReceivedNotes(page, memberCode, noteSentDate);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(notes);
        final PagingResponse pagingResponse = PagingResponse.of(notes.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);

    }

    //상세 조회
    @GetMapping("/received/{noteCode}")
    public ResponseEntity<NoteResponse> getReceivedNote(@PathVariable final Long noteCode) {

        final NoteResponse noteResponse = noteService.getReceivedNote(noteCode);

        return ResponseEntity.ok(noteResponse);

    }

    //삭제
    @DeleteMapping("/received/{noteCode}")
    public ResponseEntity<Void> deleteReceivedNote(@PathVariable final Long noteCode) {

        noteService.deleteReceivedNote(noteCode);

        return ResponseEntity.noContent().build();

    }

    //검색 조회
    @GetMapping("/received/search")
    public ResponseEntity<PagingResponse> getReceivedNoteBySearch(
            @RequestParam(defaultValue = "1") final Integer page, @RequestParam String searchCondition, @RequestParam String searchValue) {

        final Page<NotesResponse> notes = noteService.getReceivedNoteByNoteReceiverOrNoteBody(page, searchCondition, searchValue);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(notes);
        final PagingResponse pagingResponse = PagingResponse.of(notes.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);

    }


    /***************************************************************************************************************/

    //쪽지 이동
    @PutMapping("/move")
    public ResponseEntity<Void> moveNote(@RequestBody @Valid final NoteMoveRequest noteRequest) {

        noteService.moveNote(noteRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build(); //201 응답

    }


}
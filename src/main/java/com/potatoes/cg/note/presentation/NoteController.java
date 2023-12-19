package com.potatoes.cg.note.presentation;

import com.potatoes.cg.common.paging.Pagenation;
import com.potatoes.cg.common.paging.PagingButtonInfo;
import com.potatoes.cg.common.paging.PagingResponse;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.dto.response.AdminMembersResponse;
import com.potatoes.cg.note.dto.request.NoteCreateRequest;
import com.potatoes.cg.note.dto.request.NoteMoveRequest;
import com.potatoes.cg.note.dto.request.NoteReplyCreateRequest;
import com.potatoes.cg.note.dto.response.NoteMemberListResponse;
import com.potatoes.cg.note.dto.response.NoteResponse;
import com.potatoes.cg.note.dto.response.NotesResponse;
import com.potatoes.cg.note.service.NoteService;
import com.potatoes.cg.project.dto.response.ProjectMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/cg-api/v1/note/")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;


    /********************************************* 보낸 쪽지함 *********************************************/

    /* 1. 전체 조회 */
    @GetMapping("/sent")
    public ResponseEntity<PagingResponse> getSentNotes(
            @RequestParam(defaultValue = "1") final Integer page,
            @AuthenticationPrincipal CustomUser customUser
    ) {

        LocalDate noteSentDate = LocalDate.now();
        LocalDateTime search = LocalDateTime.of(noteSentDate, LocalTime.MAX); //조회기준 : 현재 날짜의 23:59:59

        final Page<NotesResponse> notes = noteService.getSentNotes(page, customUser, search);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(notes);
        final PagingResponse pagingResponse = PagingResponse.of(notes.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);

    }

    /* 2. 상세 조회 */
    @GetMapping("/sent/{noteCode}")
    public ResponseEntity<NoteResponse> getSentNote(@PathVariable final Long noteCode) {

        final NoteResponse noteResponse = noteService.getSentNote(noteCode);

        return ResponseEntity.ok(noteResponse);

    }

    /* 3. 삭제 */
    @DeleteMapping("/sent/{noteCode}")
    public ResponseEntity<Void> deleteSentNote(@PathVariable final Long noteCode) {

        noteService.deleteSentNote(noteCode);

        return ResponseEntity.noContent().build();

    }

    /* 4. 검색 조회 */
    @GetMapping("sent/search")
    public ResponseEntity<PagingResponse> getSentNoteBySearch(
            @RequestParam(defaultValue = "1") final Integer page, @RequestParam String searchCondition, @RequestParam String searchValue) {

        final Page<NotesResponse> notes = noteService.getSentNoteByNoteSenderOrNoteBody(page, searchCondition, searchValue);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(notes);
        final PagingResponse pagingResponse = PagingResponse.of(notes.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);

    }


    /********************************************* 받은 쪽지함 *********************************************/

    /* 5. 전체 조회 */
    @GetMapping("/")
    public ResponseEntity<PagingResponse> getReceivedNotes(
            @RequestParam(defaultValue = "1") final Integer page,
            @AuthenticationPrincipal CustomUser customUser
            ) {

        LocalDate noteSentDate = LocalDate.now();
        LocalDateTime search = LocalDateTime.of(noteSentDate, LocalTime.MAX);

        final Page<NotesResponse> notes = noteService.getReceivedNotes(page, customUser, search);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(notes);
        final PagingResponse pagingResponse = PagingResponse.of(notes.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);

    }

    /* 6. 상세 조회 */
    @GetMapping("/received/{noteCode}")
    public ResponseEntity<NoteResponse> getReceivedNote(@PathVariable final Long noteCode) {

        final NoteResponse noteResponse = noteService.getReceivedNote(noteCode);

        return ResponseEntity.ok(noteResponse);

    }

    /* 7. 삭제 */
    @DeleteMapping("/received/{noteCode}")
    public ResponseEntity<Void> deleteReceivedNote(@PathVariable final Long noteCode) {

        noteService.deleteReceivedNote(noteCode);

        return ResponseEntity.noContent().build();

    }

    /* 8. 검색 조회 */
    @GetMapping("/received/search")
    public ResponseEntity<PagingResponse> getReceivedNoteBySearch(
            @RequestParam(defaultValue = "1") final Integer page, @RequestParam String searchCondition, @RequestParam String searchValue) {

        final Page<NotesResponse> notes = noteService.getReceivedNoteByNoteReceiverOrNoteBody(page, searchCondition, searchValue);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(notes);
        final PagingResponse pagingResponse = PagingResponse.of(notes.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);

    }


    /********************************************* 중요 쪽지함 *********************************************/

    /* 9. 전체 조회 */
    @GetMapping("/important")
    public ResponseEntity<PagingResponse> getImportantNotes(
            @RequestParam(defaultValue = "1") final Integer page,
            @AuthenticationPrincipal CustomUser customUser
    ) {

        LocalDate noteSentDate = LocalDate.now();
        LocalDateTime search = LocalDateTime.of(noteSentDate, LocalTime.MAX);

        final Page<NotesResponse> notes = noteService.getImportantNotes(page, customUser, search);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(notes);
        final PagingResponse pagingResponse = PagingResponse.of(notes.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);

    }

    /* 10. 상세 조회 */
    @GetMapping("/important/{noteCode}")
    public ResponseEntity<NoteResponse> getImportantNote(@PathVariable final Long noteCode) {

        final NoteResponse noteResponse = noteService.getImportantNote(noteCode);

        return ResponseEntity.ok(noteResponse);

    }

    /* 11. 삭제 */
    @DeleteMapping("/important/{noteCode}")
    public ResponseEntity<Void> deleteImportantNote(@PathVariable final Long noteCode) {

        noteService.deleteImportantNote(noteCode);

        return ResponseEntity.noContent().build();

    }

    /* 12. 검색 조회 */
    @GetMapping("/important/search")
    public ResponseEntity<PagingResponse> getImportantNoteBySearch(
            @RequestParam(defaultValue = "1") final Integer page, @RequestParam String searchCondition, @RequestParam String searchValue) {

        final Page<NotesResponse> notes = noteService.getImportantNoteByMemberOrNoteBody(page, searchCondition, searchValue);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(notes);
        final PagingResponse pagingResponse = PagingResponse.of(notes.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);

    }


    /***************************************************************************************************************/

    /* 13. 이동 */
    @PutMapping("/move/{noteCode}")
    public ResponseEntity<Void> moveNote(@PathVariable Long noteCode) {

        noteService.moveNote(noteCode);

        return ResponseEntity.status(HttpStatus.CREATED).build(); //201 응답

    }

    /* 14. 쓰기 */
    @PostMapping("/send")
    public ResponseEntity<Void> postNote(
            @RequestBody @Valid final NoteCreateRequest noteRequest,
            @AuthenticationPrincipal CustomUser customUser) {

        noteService.postNote(noteRequest, customUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    /* 15. 회원 조회 */
    @GetMapping("/member/search")
    public ResponseEntity<PagingResponse> getMemberSearch(@RequestParam(defaultValue = "1") final Integer page,
                                                                @RequestParam final String infoName){

        final Page<ProjectMemberResponse> members = noteService.getMemberSearch(page, infoName);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(members);
        final PagingResponse pagingResponse = PagingResponse.of(members.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);

    }

    /* 회원 목록 조회 */
    @GetMapping("/member/list")
    public ResponseEntity<PagingResponse> getNoteListMembers(@RequestParam(defaultValue = "1") final Integer page ) {

        final Page<NoteMemberListResponse> members = noteService.getNoteListMembers(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(members);
        final PagingResponse pagingResponse = PagingResponse.of(members.getContent(), pagingButtonInfo);

        return ResponseEntity.ok( pagingResponse );

    }

    /* 답장 */
    @PostMapping("/replySend")
    public ResponseEntity<Void> postReplyNote(
            @RequestBody @Valid final NoteReplyCreateRequest noteRequest,
            @AuthenticationPrincipal CustomUser customUser) {

        System.out.println(noteRequest + "noteRequest");
        noteService.postReplyNote(noteRequest, customUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

}
package com.potatoes.cg.note.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.member.domain.repository.MemberRepository;
import com.potatoes.cg.member.dto.response.AdminMembersResponse;
import com.potatoes.cg.note.domain.Note;
import com.potatoes.cg.note.domain.repository.NoteRepository;
import com.potatoes.cg.note.domain.type.NoteStatusType;
import com.potatoes.cg.note.dto.request.NoteCreateRequest;
import com.potatoes.cg.note.dto.request.NoteMoveRequest;
import com.potatoes.cg.note.dto.request.NoteReplyCreateRequest;
import com.potatoes.cg.note.dto.response.NoteMemberListResponse;
import com.potatoes.cg.note.dto.response.NoteResponse;
import com.potatoes.cg.note.dto.response.NotesResponse;
import com.potatoes.cg.project.domain.repository.ProjectMemberRepository;
import com.potatoes.cg.project.dto.response.ProjectMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;

import static com.potatoes.cg.common.exception.type.ExceptionCode.*;
import static com.potatoes.cg.member.domain.type.MemberStatus.ACTIVE;
import static com.potatoes.cg.note.domain.type.NoteStatusType.*;


@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;
    private final MemberRepository memberRepository;
    private final InfoRepository infoRepository;
    private final ProjectMemberRepository projectMemberRepository;

    private Pageable getPageable(final Integer page) {

        return PageRequest.of(page -1, 6, Sort.by("noteCode").descending());

    }

    private Pageable getPage(final Integer page) {

        return PageRequest.of(page -1, 7, Sort.by("memberCode").descending());

    }
    /********************************************* 보낸 쪽지함 *********************************************/

    /* 1. 전체 조회 */
    @Transactional(readOnly = true)
    public Page<NotesResponse> getSentNotes(@RequestParam(defaultValue = "1") final int page,
                                            final CustomUser customUser,
                                            LocalDateTime search) {

        Page<Note> notes = noteRepository.findByNoteSenderMemberCodeAndNoteSentDateBeforeAndNoteSenderStatus
                (getPageable(page), customUser.getMemberCode(), search, DEFAULT);

        return notes.map(note -> NotesResponse.from(note));

    }

    /* 2. 상세 조회 */
    @Transactional(readOnly = true)
    public NoteResponse getSentNote(final Long noteCode) {

        Note note = noteRepository.findById(noteCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_NOTE_CODE));

        return NoteResponse.from(note);

    }

    /* 3. 삭제 */
    public void deleteSentNote(final Long noteCode) {

        noteRepository.deleteById(noteCode);

    }

    /* 4. 검색 조회 */
    @Transactional(readOnly = true)
    public Page<NotesResponse> getSentNoteByNoteSenderOrNoteBody(@RequestParam(defaultValue = "1") final Integer page,
                                                                 final String searchCondition, final String searchValue) {

        Page<Note> notes = null;

        if (searchCondition.equals("all")) {
            notes = noteRepository.findByNoteSentSearchAll
                    (getPageable(page), searchValue, searchValue, DEFAULT);
        } else if (searchCondition.equals("noteSender")) {
            notes = noteRepository.findByNoteSenderMemberInfoInfoNameContainsAndNoteSenderStatus(getPageable(page), searchValue, DEFAULT);
        } else if (searchCondition.equals("noteBody")) {
            notes = noteRepository.findByNoteBodyContainsAndNoteSenderStatus(getPageable(page), searchValue, DEFAULT);
        } else {
            new NotFoundException(NOT_FOUND_NOTE);
        }

        return notes.map(note -> NotesResponse.from(note));

    }


    /********************************************* 받은 쪽지함 *********************************************/

    /* 5. 전체 조회 */
    @Transactional(readOnly = true)
    public Page<NotesResponse> getReceivedNotes(@RequestParam(defaultValue = "1") final int page,
                                                final CustomUser customUser, LocalDateTime search) {

        Page<Note> notes = noteRepository.findByNoteReceiverMemberCodeAndNoteSentDateBeforeAndNoteReceiverStatus(
                getPageable(page), customUser.getMemberCode(), search, DEFAULT);

        return notes.map(note -> NotesResponse.from(note));

    }

    /* 6. 상세 조회 */
    @Transactional(readOnly = true)
    public NoteResponse getReceivedNote(final Long noteCode) {

        Note note = noteRepository.findById(noteCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_NOTE_CODE));

        return NoteResponse.from(note);

    }

    /* 7. 삭제 */
    public void deleteReceivedNote(final Long noteCode) {

        Optional<Note> optionalNote = noteRepository.findById(noteCode);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            note.setNoteReceiverStatus(DELETED);
            noteRepository.save(note);
        }

    }

    /* 8. 검색 조회 */
    public Page<NotesResponse> getReceivedNoteByNoteReceiverOrNoteBody(@RequestParam(defaultValue = "1") final Integer page,
                                                                       final String searchCondition, final String searchValue) {

        Page<Note> notes = null;

        if (searchCondition.equals("all")) {
            notes = noteRepository.findByNoteReceivedSearchAll(
                    getPageable(page), searchValue, searchValue, DEFAULT);
        } else if (searchCondition.equals("noteReceiver")) {
            notes = noteRepository.findByNoteReceiverMemberInfoInfoNameContainsAndNoteReceiverStatus(getPageable(page), searchValue, DEFAULT);
        } else if (searchCondition.equals("noteBody")) {
            notes = noteRepository.findByNoteBodyContainsAndNoteReceiverStatus(getPageable(page), searchValue, DEFAULT);
        } else {
            new NotFoundException(NOT_FOUND_NOTE);
        }

        return notes.map(note -> NotesResponse.from(note));

    }


    /********************************************* 중요 쪽지함 *********************************************/

    /* 9. 전체 조회 */
    @Transactional(readOnly = true)
    public Page<NotesResponse> getImportantNotes(@RequestParam(defaultValue = "1")final int page,
                                                 final CustomUser customUser, LocalDateTime search) {

        Page<Note> notes = noteRepository.findByAllMemberCodeAndNoteDateAndAllNoteStatus(
                getPageable(page), customUser.getMemberCode(), search, IMPORTANT);

        return notes.map(note -> NotesResponse.from(note));

    }

    /* 10. 상세 조회 */
    @Transactional(readOnly = true)
    public NoteResponse getImportantNote(final Long noteCode) {

        Note note = noteRepository.findById(noteCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_NOTE_CODE));

        return NoteResponse.from(note);

    }

    /* 11. 삭제 */
    public void deleteImportantNote(final Long noteCode) {

        noteRepository.deleteById(noteCode);

    }

    /* 12. 검색 조회 */
    public Page<NotesResponse> getImportantNoteByMemberOrNoteBody(@RequestParam(defaultValue = "1")final Integer page,
                                                                  final String searchCondition, final String searchValue) {

        Page<Note> notes = null;

        if (searchCondition.equals("all")) {
            notes = noteRepository.findByImportantNoteSearchAll(
                    getPageable(page), searchValue, searchValue, searchValue, IMPORTANT, IMPORTANT);
        } else if (searchCondition.equals("noteSender")) {
            notes = noteRepository.findByNoteSenderMemberInfoInfoNameContainsAndNoteSenderStatus(getPageable(page), searchValue, IMPORTANT);
        } else if (searchCondition.equals("noteReceiver")) {
            notes = noteRepository.findByNoteReceiverMemberInfoInfoNameContainsAndNoteReceiverStatus(getPageable(page), searchValue, IMPORTANT);
        } else if (searchCondition.equals("noteBody")) {
            notes = noteRepository.findByNoteBodyContainsAndNoteStatus(getPageable(page), searchValue, IMPORTANT);
        } else {
            new NotFoundException(NOT_FOUND_NOTE);
        }

        return notes.map(note -> NotesResponse.from(note));

    }


    /***************************************************************************************************************/

    /* 13. 이동 */
    @Transactional
    public void moveNote(Long noteCode) {

        Note note = noteRepository.findById(noteCode).orElseThrow();

        if(note.getNoteReceiverStatus() == DEFAULT) {
            note.setNoteReceiverStatus(IMPORTANT);
        } else if(note.getNoteReceiverStatus() == IMPORTANT) {
            note.setNoteReceiverStatus(DEFAULT);
        }

        noteRepository.save(note);

    }

    /***************************************************************************************************************/

    /* 14. 쓰기 */
    public void postNote(final NoteCreateRequest noteRequest, CustomUser customUser) {

       Member noteSender = memberRepository.getReferenceById(customUser.getMemberCode());
       Member noteReceiver = memberRepository.getReferenceById(noteRequest.getNoteReceiver());

       Note newNote = Note.of(noteSender, noteReceiver, noteRequest.getNoteBody());

       noteRepository.save(newNote);

    }

    /* 15. 회원 조회 */
    @Transactional(readOnly = true)
    public Page<ProjectMemberResponse> getMemberSearch(Integer page, String infoName) {

        Page<Member> members = projectMemberRepository.findByMemberInfoInfoNameContainsAndMemberStatus(getPage(page), infoName, ACTIVE);

        return members.map(Member -> ProjectMemberResponse.fromMember(Member));

    }

    /* 회원 목록 조회 */
    @Transactional(readOnly = true)
    public Page<NoteMemberListResponse> getNoteListMembers(Integer page) {

        Page<Member> memberList = memberRepository.findAll(getPage(page));

        return memberList.map( member -> NoteMemberListResponse.from(member));
    }

    /* 답장 */
    public void postReplyNote(NoteReplyCreateRequest noteRequest, CustomUser customUser) {
        Member noteSender = memberRepository.getReferenceById(customUser.getMemberCode());
        MemberInfo info = infoRepository.findByInfoName(noteRequest.getNoteReceiver());
        Member noteReceiver = memberRepository.findByMemberInfo(info);

        System.out.println(noteReceiver + "noteReceiver");
        Note newNote = Note.of(noteSender, noteReceiver, noteRequest.getNoteBody());

        noteRepository.save(newNote);
    }

}
package com.potatoes.cg.note.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.note.domain.Note;
import com.potatoes.cg.note.domain.repository.NoteRepository;
import com.potatoes.cg.note.domain.type.NoteStatusType;
import com.potatoes.cg.note.dto.request.NoteMoveRequest;
import com.potatoes.cg.note.dto.response.NoteResponse;
import com.potatoes.cg.note.dto.response.NotesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.potatoes.cg.common.exception.type.ExceptionCode.*;
import static com.potatoes.cg.note.domain.type.NoteStatusType.*;


@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;

    private Pageable getPageable(@RequestParam(defaultValue = "1") final Integer page) {

        return PageRequest.of(page -1, 10, Sort.by("noteCode").descending());

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
    public void moveNote(NoteMoveRequest noteRequest) {

        List<Note> notes = noteRepository.findAllById(noteRequest.getNoteCodes());

        //받은 쪽지인지 보낸 쪽지인지 판단해서 분기하고 컬럼에 따른 수정 (스트림, forEach)
        notes.forEach(note -> {
            if (noteRequest.getNoteDivision().equals("sent")) {
                moveSentNote(note, noteRequest.getNoteStatusType());
            } else if (noteRequest.getNoteDivision().equals("received")) {
                moveReceivedNote(note, noteRequest.getNoteStatusType());
            }
        });

        noteRepository.saveAll(notes);

    }

    private void moveSentNote(Note note, NoteStatusType noteStatusType) {

        switch (noteStatusType) {
            case DEFAULT:
                note.setNoteSenderStatus(DEFAULT);
                break;
            case IMPORTANT:
                note.setNoteSenderStatus(IMPORTANT);
                break;
        }

    }

    private void moveReceivedNote(Note note, NoteStatusType noteStatusType) {

        switch (noteStatusType) {
            case DEFAULT:
                note.setNoteReceiverStatus(DEFAULT);
                break;
            case IMPORTANT:
                note.setNoteReceiverStatus(IMPORTANT);
                break;
        }

    }

    /***************************************************************************************************************/

}
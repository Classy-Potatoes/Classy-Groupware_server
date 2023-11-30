package com.potatoes.cg.note.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.note.domain.Note;
import com.potatoes.cg.note.domain.repository.NoteRepository;
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

import java.time.LocalDateTime;
import static com.potatoes.cg.note.domain.type.NoteStatusType.DEFAULT;

import static com.potatoes.cg.common.exception.type.ExceptionCode.*;


@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;

    private Pageable getPageable(final Integer page) {

        return PageRequest.of(page -1, 10, Sort.by("noteCode").descending());

    }

    //보낸 쪽지 전체 조회
    @Transactional(readOnly = true)
    public Page<NotesResponse> getSentNotes(final int page, final Long memberCode, LocalDateTime noteSentDate) {

        Page<Note> notes = noteRepository.findByNoteSenderMemberCodeAndNoteSentDateBeforeAndNoteSenderStatus(getPageable(page), memberCode, noteSentDate, DEFAULT);

        return notes.map(note -> NotesResponse.from(note));

    }

    //보낸 쪽지 상세 조회
    @Transactional(readOnly = true)
    public NoteResponse getSentNote(final Long noteCode) {

        Note note = noteRepository.findById(noteCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_NOTE_CODE));

        return NoteResponse.from(note);

    }

    //받은 쪽지 전체 조회
    @Transactional(readOnly = true)
    public Page<NotesResponse> getReceivedNotes(final int page, final Long memberCode, LocalDateTime noteSentDate) {

        Page<Note> notes = noteRepository.findByNoteReceiverMemberCodeAndNoteSentDateBeforeAndNoteReceiverStatus(getPageable(page), memberCode, noteSentDate, DEFAULT);

        return notes.map(note -> NotesResponse.from(note));

    }

    //받은 쪽지 상세 조회
    @Transactional(readOnly = true)
    public NoteResponse getReceivedNote(final Long noteCode) {

        Note note = noteRepository.findById(noteCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_NOTE_CODE));

        return NoteResponse.from(note);

    }

    //보낸 쪽지 삭제
    public void deleteSentNote(final Long noteCode) {

        noteRepository.deleteById(noteCode);

    }

    //받은 쪽지 삭제
    public void deleteReceivedNote(final Long noteCode) {

        noteRepository.deleteById(noteCode);

    }

    //보낸 쪽지함 - 이름 검색
//    @Transactional(readOnly = true)
//    public Page<NotesResponse> getSentNotesByNoteSender(final Integer page, final String noteSender) {
//
//        Page<Note> notes = noteRepository.findByNoteSenderContainsAndNoteSenderStatus(getPageable(page), noteSender, DEFAULT);
//
//        return notes.map(note -> NotesResponse.from(note));
//
//    }

    //보낸 쪽지함 검색 조회
    @Transactional(readOnly = true)
    public Page<NotesResponse> getSentNoteByNoteSenderOrNoteBody(final Integer page, final String searchCondition, final String searchValue) {

        Page<Note> notes = null;

        if (searchCondition.equals("all")) {
            notes = noteRepository.findBySearchAll
                    (getPageable(page), searchValue, searchValue, DEFAULT);
        }
        else if (searchCondition.equals("noteSender")) {
            notes = noteRepository.findByNoteSenderMemberInfoInfoNameContainsAndNoteSenderStatus(getPageable(page), searchValue, DEFAULT);
        } else if (searchCondition.equals("noteBody")) {
            notes = noteRepository.findByNoteBodyContainsAndNoteSenderStatus(getPageable(page), searchValue, DEFAULT);
        } else {
            new NotFoundException(NOT_FOUND_NOTE);
        }

        return notes.map(note -> NotesResponse.from(note));

    }

    //쪽지 이동
//    @Transactional
//    public void move(final Long noteCode, final NoteMoveRequest noteRequest) {
//
//        Note note = noteRepository.findById(noteRequest.getNoteCode())
//                .orElseThrow(() -> new NotFoundException(NOT_FOUND_NOTE_CODE));
//
//        note.move(
//                noteRequest.getNoteSender(),
//                noteRequest.getNoteReceiver(),
//                noteRequest.getNoteBody()
//        );
//
//    }

    //쪽지 쓰기
//    public Long save(final NoteCreateRequest noteRequest, final CustomUser customUser) {
//
//        Note note = noteRepository.findById(customUser.getInfoCode().)
//                .orElseThrow(() -> new NotFoundException(NOT_FOUND_INFO_NAME));
//
//        final Note newNote = Note.of(
//
//        );
//
//        final Note note = noteRepository.save(newNote);
//
//        return note.getNoteCode();
//
//    }

}
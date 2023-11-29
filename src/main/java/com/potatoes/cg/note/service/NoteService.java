package com.potatoes.cg.note.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.note.domain.Note;
import com.potatoes.cg.note.domain.repository.NoteRepository;
import com.potatoes.cg.note.dto.request.NoteCreateRequest;
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

import static com.potatoes.cg.common.exception.type.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;

    private Pageable getPageable(final Integer page) {

        return PageRequest.of(page -1, 5, Sort.by("noteCode").descending());

    }

    //보낸 쪽지 전체 조회
    @Transactional(readOnly = true)
    public Page<NotesResponse> sentNotes(final int page, final Long memberCode, LocalDateTime noteSentDate) {

        Page<Note> notes = noteRepository.findByNoteSenderMemberCodeAndNoteSentDateBefore(getPageable(page), memberCode, noteSentDate);

        return notes.map(note -> NotesResponse.from(note));

    }

    //보낸 쪽지 상세 조회
    @Transactional(readOnly = true)
    public NoteResponse sentNote(final Long noteCode) {

        Note note = noteRepository.findById(noteCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_NOTE_CODE));

        return NoteResponse.from(note);

    }

    //받은 쪽지 전체 조회
    @Transactional(readOnly = true)
    public Page<NotesResponse> receivedNotes(final int page, final Long memberCode, LocalDateTime noteSentDate) {

        Page<Note> notes = noteRepository.findByNoteReceiverMemberCodeAndNoteSentDateBefore(getPageable(page), memberCode, noteSentDate);

        return notes.map(note -> NotesResponse.from(note));

    }

    //받은 쪽지 상세 조회
    @Transactional(readOnly = true)
    public NoteResponse receivedNote(final Long noteCode) {

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
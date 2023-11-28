package com.potatoes.cg.note.service;

import com.potatoes.cg.note.domain.Note;
import com.potatoes.cg.note.domain.repository.NoteRepository;
import com.potatoes.cg.note.dto.response.NoteResponse;
import com.potatoes.cg.note.dto.response.NotesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;

    private Pageable getPageable(final Integer page) {

        return PageRequest.of(page -1, 5, Sort.by("noteCode").descending());

    }

    @Transactional(readOnly = true)
    public Page<NotesResponse> sentNotes(final int page, final Long memberCode, LocalDateTime noteSentDate) {

        Page<Note> notes = noteRepository.findByNoteSenderMemberCodeAndNoteSentDateBefore(getPageable(page), memberCode, noteSentDate);

        return notes.map(note -> NotesResponse.from(note));

    }

//    @Transactional(readOnly = true)
//    public NoteResponse sentNote(Long noteCode) {
//
//        final Note note = noteRepository.findById(noteCode)
//                .orElseThrow() -> new ChangeSetPersister.NotFoundException()
//    }

}
package com.potatoes.cg.note.domain.repository;

import com.potatoes.cg.note.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface NoteRepository extends JpaRepository<Note, Long> {

    //보낸 쪽지 날짜별 조회
    Page<Note> findByNoteSenderMemberCodeAndNoteSentDateBefore (Pageable pageable, Long MemberCode, LocalDateTime noteSentDate);

    //받은 쪽지 날짜별 조회
    Page<Note> findByNoteReceiverMemberCodeAndNoteSentDateBefore (Pageable pageable, Long MemberCode, LocalDateTime noteSentDate);



}

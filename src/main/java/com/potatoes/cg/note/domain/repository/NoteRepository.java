package com.potatoes.cg.note.domain.repository;

import com.potatoes.cg.note.domain.Note;
import com.potatoes.cg.note.domain.type.NoteStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface NoteRepository extends JpaRepository<Note, Long> {

    //보낸 쪽지 전체 조회
    Page<Note> findByNoteSenderMemberCodeAndNoteSentDateBeforeAndNoteSenderStatus(Pageable pageable, Long memberCode, LocalDateTime noteSentDate, NoteStatusType noteStatusType);

    //받은 쪽지 전체 조회
    Page<Note> findByNoteReceiverMemberCodeAndNoteSentDateBeforeAndNoteReceiverStatus(Pageable pageable, Long memberCode, LocalDateTime noteSentDate, NoteStatusType noteStatusType);

    //보낸 쪽지함 검색 - 내용
    Page<Note> findByNoteBodyContainsAndNoteSenderStatus(Pageable pageable, String searchValue, NoteStatusType noteStatusType);

    //보낸 쪽지함 검색 - 이름
    Page<Note> findByNoteSenderMemberInfoInfoNameContainsAndNoteSenderStatus(Pageable pageable, String searchValue, NoteStatusType noteStatusType);

    //보낸 쪽지함 검색 - 전체
    @EntityGraph(attributePaths = { "noteSender", "noteSender.memberInfo", "noteReceiver", "noteReceiver.memberInfo" })
    @Query(value = "select n from Note n where (n.noteBody like %:noteBody% or n.noteSender.memberInfo.infoName like %:noteSender%) and n.noteSenderStatus = :noteSenderStatus")
    Page<Note> findBySearchAll(Pageable pageable, @Param(value = "noteBody") String noteBody, @Param(value = "noteSender") String noteSender, @Param(value = "noteSenderStatus") NoteStatusType noteStatusType);

}

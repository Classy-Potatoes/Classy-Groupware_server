package com.potatoes.cg.note.domain.repository;

import com.potatoes.cg.note.domain.Note;
import com.potatoes.cg.note.domain.type.NoteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface NoteRepository extends JpaRepository<Note, Long> {

    /********************************************* 보낸 쪽지함 *********************************************/

    //전체 조회
    Page<Note> findByNoteSenderMemberCodeAndNoteSentDateBeforeAndNoteSenderStatus(Pageable pageable, Long memberCode, LocalDateTime noteSentDate, NoteStatus noteStatus);

    //검색 - 받는 사람
    Page<Note> findByNoteSenderMemberInfoInfoNameContainsAndNoteSenderStatus(Pageable pageable, String searchValue, NoteStatus noteStatus);

    //검색 - 내용
    Page<Note> findByNoteBodyContainsAndNoteSenderStatus(Pageable pageable, String searchValue, NoteStatus noteStatus);

    //검색 - 전체
    @EntityGraph(attributePaths = { "noteSender", "noteSender.memberInfo", "noteReceiver", "noteReceiver.memberInfo" }
    )
    @Query(value = "select n from Note n " +
            "where (n.noteBody like %:noteBody% or n.noteSender.memberInfo.infoName like %:noteSender%) " +
            "and n.noteSenderStatus = :noteSenderStatus")
    Page<Note> findByNoteSentSearchAll(Pageable pageable,
                                       @Param(value = "noteBody") String noteBody,
                                       @Param(value = "noteSender") String noteSender,
                                       @Param(value = "noteSenderStatus") NoteStatus noteStatus);


    /********************************************* 받은 쪽지함 *********************************************/

    //전체 조회
    Page<Note> findByNoteReceiverMemberCodeAndNoteSentDateBeforeAndNoteReceiverStatus(Pageable pageable, Long memberCode, LocalDateTime noteSentDate, NoteStatus noteStatus);

    //검색 - 보낸 사람
    Page<Note> findByNoteReceiverMemberInfoInfoNameContainsAndNoteReceiverStatus(Pageable pageable, String searchValue, NoteStatus noteStatus);

    //검색 - 내용
    Page<Note> findByNoteBodyContainsAndNoteReceiverStatus(Pageable pageable, String searchValue, NoteStatus noteStatus);

    //검색 - 전체
    @EntityGraph(attributePaths = { "noteSender", "noteSender.memberInfo", "noteReceiver", "noteReceiver.memberInfo" })
    @Query(value = "select n from Note n " +
            "where (n.noteBody like %:noteBody% or n.noteSender.memberInfo.infoName like %:noteReceiver%) " +
            "and n.noteReceiverStatus = :noteReceiverStatus")
    Page<Note> findByNoteReceivedSearchAll(Pageable pageable,
                                           @Param(value = "noteBody") String noteBody,
                                           @Param(value = "noteReceiver") String noteReceiver,
                                           @Param(value = "noteReceiverStatus") NoteStatus noteStatus);


    /********************************************* 중요 쪽지함 *********************************************/

    //전체 조회
    @Query(value = "select n from Note n " +
            "where (n.noteReceiver.memberCode = :memberCode " +
            "or n.noteSender.memberCode = :memberCode) " +
            "and n.noteSentDate < :noteSentDate " +
            "and (n.noteReceiverStatus = :noteStatus) " +
            "or n.noteSenderStatus = :noteStatus")
    Page<Note> findByAllMemberCodeAndNoteDateAndAllNoteStatus(
            Pageable pageable,
            @Param("memberCode") Long memberCode,
            @Param("noteSentDate") LocalDateTime noteSentDate,
            @Param("noteStatus") NoteStatus noteStatus);

    //검색 - 내용
    @Query(value = "select n from Note n " +
            "where (n.noteBody like %:searchValue% ) " +
            "and (n.noteReceiverStatus = :noteStatus " +
            "or n.noteSenderStatus = :noteStatus)")
    Page<Note> findByNoteBodyContainsAndNoteStatus(Pageable pageable,
                                                   @Param("searchValue") String searchValue,
                                                   @Param("noteStatus") NoteStatus noteStatus);

    //검색 - 전체
    @Query(value = "select n from Note n " +
            "where (n.noteBody like %:noteBody%) " +
            "or (n.noteSender.memberInfo.infoName like %:noteSender%) " +
            "or (n.noteReceiver.memberInfo.infoName like %:noteReceiver%) " +
            "and (n.noteReceiverStatus = :noteReceiverStatus " +
            "or n.noteSenderStatus = :noteSenderStatus)")
    Page<Note> findByImportantNoteSearchAll(Pageable pageable,
                                            @Param(value = "noteBody") String noteBody,
                                            @Param(value = "noteSender") String noteSender,
                                            @Param(value = "noteReceiver") String noteReceiver,
                                            @Param(value = "noteSenderStatus") NoteStatus noteSenderStatus,
                                            @Param(value = "noteReceiverStatus") NoteStatus noteReceiverStatus);

}

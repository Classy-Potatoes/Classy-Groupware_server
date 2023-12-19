package com.potatoes.cg.note.domain;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.note.domain.type.NoteReceiptStatus;
import com.potatoes.cg.note.domain.type.NoteStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.potatoes.cg.note.domain.type.NoteReceiptStatus.UNREAD;
import static com.potatoes.cg.note.domain.type.NoteStatusType.DEFAULT;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Entity
@Table(name = "TBL_NOTE")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Note {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long noteCode;

    @Column(nullable = false)
    private String noteBody;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime noteSentDate;

    @Column
    private LocalDate noteSenderDeleteDate;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private NoteReceiptStatus noteReceiptStatus = UNREAD;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private NoteStatusType noteSenderStatus = DEFAULT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noteSender")
    private Member noteSender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noteReceiver")
    private Member noteReceiver;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private NoteStatusType noteReceiverStatus = DEFAULT;

    @Column
    private LocalDate noteReceiverDeleteDate;

    public void setNoteSenderStatus(NoteStatusType noteStatusType) {
        this.noteSenderStatus = noteStatusType;
    }

    public void setNoteReceiverStatus(NoteStatusType noteStatusType) {
        this.noteReceiverStatus = noteStatusType;
    }

    public void setNoteStatusType(NoteStatusType noteStatusType) {
        this.noteReceiverStatus = noteStatusType;
    }

    public Note(Member noteSender, Member noteReceiver, String noteBody) {
        this.noteSender = noteSender;
        this.noteReceiver = noteReceiver;
        this.noteBody = noteBody;
    }

    public static Note of(Member noteSender, Member noteReceiver, String noteBody) {

        return new Note(
                noteSender,
                noteReceiver,
                noteBody
        );


    }
}
package com.potatoes.cg.note.domain;

import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.note.domain.type.NoteReceiptStatus;
import com.potatoes.cg.note.domain.type.NoteStatus;
import com.potatoes.cg.note.dto.request.NoteCreateRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.potatoes.cg.note.domain.type.NoteReceiptStatus.UNREAD;
import static com.potatoes.cg.note.domain.type.NoteStatus.DEFAULT;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

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

    @Column(nullable = false)
    private LocalDateTime noteSentDate;

    @Column
    private LocalDateTime noteSenderDeleteDate;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private NoteReceiptStatus noteReceiptStatus = UNREAD;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private NoteStatus noteSenderStatus = DEFAULT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noteSender")
    private Member noteSender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noteReceiver")
    private Member noteReceiver;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private NoteStatus noteReceiverStatus = DEFAULT;

    @Column
    private LocalDateTime noteReceiverDeleteDate;

    public void setNoteSenderStatus(NoteStatus noteStatus) {
        this.noteSenderStatus = noteStatus;
    }

    public void setNoteReceiverStatus(NoteStatus noteStatus) {
        this.noteReceiverStatus = noteStatus;
    }

}
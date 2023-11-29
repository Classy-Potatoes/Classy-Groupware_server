package com.potatoes.cg.note.domain;

import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private String noteReceiptStatus;

    @Column(nullable = false)
    private String noteSenderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noteSender")
    private Member noteSender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noteReceiver")
    private Member noteReceiver;

    @Column(nullable = false)
    private String noteReceiverStatus;

    @Column
    private LocalDateTime noteReceiverDeleteDate;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "infoName")
//    @Column
//    private Member infoName;

//    public Note(String noteBody, String infoName) {
//        this.noteBody = noteBody;
//        this.infoName = infoName;
//    }
//
//    public static Note of(final String noteBody, final Member infoName) {
//
//        return new Note(
//                noteBody,
//                infoName
//        );

//    }

}
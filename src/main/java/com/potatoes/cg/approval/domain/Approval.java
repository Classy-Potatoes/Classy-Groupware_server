package com.potatoes.cg.approval.domain;

import com.potatoes.cg.approval.domain.type.ApprovalStatusType;
import com.potatoes.cg.approval.domain.type.DocumentType;
import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.potatoes.cg.approval.domain.type.ApprovalStatusType.WAITING;
import static com.potatoes.cg.approval.domain.type.DocumentType.LETTER;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_approval")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalCode;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime approvalRegistDate;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private ApprovalStatusType approvalStatus;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "memberCode")
    private Member member;

    @Column
    private String approvalTurnbackReason;


    @Column(updatable = false)
    private LocalDateTime approvalApproveDate;


    @Column(updatable = false)
    private LocalDateTime approvalTurnbackDate;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private DocumentType documentType = LETTER;

    @Column(nullable = false)
    private String documentTitle;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST )
    @JoinColumn(name = "approvalCode")
    private List<ApprovalLine> approvalLine;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "approvalCode")
    private List<Reference> referenceLine;


//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "approvalCode")
//    private List<FileEntity> fileEntity;

    public Approval(String documentTitle, List<ApprovalLine> approvalLine, List<Reference> referenceLine, DocumentType documentType) {
        this.documentTitle = documentTitle;
        this.approvalLine = approvalLine;
        this.referenceLine = referenceLine;
        this.documentType = documentType;
        this.approvalStatus = WAITING;
    }

    public static Approval of(final String documentTitle, final List<Reference> referenceLine, final List<ApprovalLine> approvalLine
                              ,final DocumentType documentType) {

        return new Approval(
                documentTitle,
                approvalLine,
                referenceLine,
                documentType

        );
    }


}

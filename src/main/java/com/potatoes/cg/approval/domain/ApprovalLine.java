package com.potatoes.cg.approval.domain;

import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineResultType;
import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineTurnType;
import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineWaitingStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_approval_line")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ApprovalLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalLineCode;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "approvalCode")
//    private Approval approval;

//    @ManyToOne(cascade = CascadeType.PERSIST)
//    @JoinColumn(name =  "memberCode")
//    private Member member;

    private Long memberCode;


    @Column(nullable = false)
    private String turn;

    @Enumerated(value = STRING)
    @Column(nullable = true)
    private ApprovalLineResultType approvalLineResult;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private ApprovalLineWaitingStatusType approvalLineWaitingStatus;

    @Column(nullable = true)
    private LocalDateTime approvalLineDate;


    public ApprovalLine(Long memberCode, String turn) {
            this.memberCode = memberCode;
            this.turn = turn;
            setApprovalLineWaitingStatus();
    }


    private void setApprovalLineWaitingStatus() {
        if ("1".equals(turn)) {
            this.approvalLineWaitingStatus = ApprovalLineWaitingStatusType.REQUEST;
        } else {
            this.approvalLineWaitingStatus = ApprovalLineWaitingStatusType.WAIT;
        }
    }

    public static ApprovalLine of(Long memberCode, String turn) {

        return new ApprovalLine(
                memberCode,
                turn
        );

    }


}

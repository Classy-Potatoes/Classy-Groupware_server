package com.potatoes.cg.approval.domain;

import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineWaitingStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_approval_line")
@Getter
@NoArgsConstructor(access = PROTECTED)
//@DynamicUpdate
public class ApprovalLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalLineCode;


    private Long memberCode;


    @Column(nullable = false)
    private String turn;


    @Column(nullable = true)
    private String approvalLineResult;

    @Enumerated(value = STRING)
    @Column(nullable = true)
    private ApprovalLineWaitingStatusType approvalLineWaitingStatus;

    @Column(nullable = true)
    private LocalDateTime approvalLineDate;


    private Long approvalCode;


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


    public void setApprovalLineWaitingStatus(ApprovalLineWaitingStatusType approvalLineWaitingStatusType) {
        this.approvalLineWaitingStatus = approvalLineWaitingStatusType;
    }

    public void setApprovalLineDate(LocalDateTime now) {
        this.approvalLineDate = now;
    }


    public void setApprovalLineResult(String approvalLineResultType) {
        this.approvalLineResult = approvalLineResultType;
    }


}

package com.potatoes.cg.approval.domain;

import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineResultType;
import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineWaitingStatusType;
import com.potatoes.cg.member.domain.Member;
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
public class ApprovalLine_GET {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalLineCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberCode")
    private Member member;


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



}


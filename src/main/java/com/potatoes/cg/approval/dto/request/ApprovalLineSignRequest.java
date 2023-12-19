package com.potatoes.cg.approval.dto.request;

import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineResultType;
import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineWaitingStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ApprovalLineSignRequest {
    private final Long approvalLineCode;
    private final ApprovalLineResultType approvalLineResultType;
    private final ApprovalLineWaitingStatusType approvalLineWaitingStatusType;
    private final LocalDateTime approvalLineDate;
}

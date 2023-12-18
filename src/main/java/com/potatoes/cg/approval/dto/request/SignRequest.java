package com.potatoes.cg.approval.dto.request;

import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineResultType;
import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineWaitingStatusType;
import com.potatoes.cg.approval.domain.type.approvalType.ApprovalStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class SignRequest {


    private final String approvalLineResult;
    private final String approvalTurnbackReson;


}

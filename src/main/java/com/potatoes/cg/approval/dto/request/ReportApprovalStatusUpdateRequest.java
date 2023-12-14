package com.potatoes.cg.approval.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class ReportApprovalStatusUpdateRequest {

    private final List<Long> approvalCode;

    @JsonCreator
    public ReportApprovalStatusUpdateRequest() {
        this(Collections.emptyList());
    }
}

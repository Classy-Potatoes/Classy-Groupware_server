package com.potatoes.cg.approval.dto.response;

import com.potatoes.cg.approval.domain.Approval;
import com.potatoes.cg.approval.domain.ApprovalFile;
import com.potatoes.cg.approval.domain.type.approvalType.ApprovalStatusType;
import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;
import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ReportResponse {

    private final Long approvalCode;
    private final DocumentType documentType;
    private final LocalDateTime approvalRegistDate;
    private final String infoName;
    private final String documentTitle;
    private final List<ApprovalFile> attachment;
    private final ApprovalStatusType approvalStatusType;

    public static ReportResponse from(final Approval approval) {
        return new ReportResponse(
                approval.getApprovalCode(),
                approval.getDocumentType(),
                approval.getApprovalRegistDate(),
                approval.getMember().getMemberInfo().getInfoName(),
                approval.getDocumentTitle(),
                approval.getAttachment(),
                approval.getApprovalStatus()
                );
    }
}

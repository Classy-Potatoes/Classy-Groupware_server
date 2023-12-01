package com.potatoes.cg.approval.dto.request;

import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ExpenseCreateRequest {

    private final List<Long> approvalLine; // 결재자

    private final List<Long> referenceLine; // 참조자

    private final DocumentType documentType; // 문서구분

}

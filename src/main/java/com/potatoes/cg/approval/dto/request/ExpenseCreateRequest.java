package com.potatoes.cg.approval.dto.request;

import com.potatoes.cg.approval.domain.ExpenseDetail;
import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;
import com.potatoes.cg.approval.domain.type.expenseType.ExpenseStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@ToString
public class ExpenseCreateRequest {

    private final String documentTitle; // 문서 제목

    private final List<Long> approvalLine; // 결재자

    private final List<Long> referenceLine; // 참조자

    private final DocumentType documentType; // 문서구분

    private final String expenseNote; // 비고

    private final ExpenseStatusType expenseStatus; // 결제 구분(법인,개인)

    private final List<Map<String, String>> expenseDetails; // 지출결의서 상세 (적요,금액,날짜,계정과목)

}

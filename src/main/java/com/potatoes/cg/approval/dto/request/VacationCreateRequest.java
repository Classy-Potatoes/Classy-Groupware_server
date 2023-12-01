package com.potatoes.cg.approval.dto.request;

import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;
import com.potatoes.cg.approval.domain.type.vacationType.VacationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@RequiredArgsConstructor
@Getter
public class VacationCreateRequest {

    private final DocumentType documentType; // 문서 타입

    private final String documentTitle; // 문서 제목

    private final List<Long> approvalLine; // 결재자 맴버코드

    private final List<Long> referenceLine; // 참조자

    private final VacationType vacationType; // 휴가구분

    private final LocalDate vacationStartDate; // 휴가 시작날짜

    private final LocalDate vacationEndDate; // 휴가 종료날짜

    private final String vacationBody; // 상세내용

    private final String vacationEmergencyPhone; // 비상연락망
}

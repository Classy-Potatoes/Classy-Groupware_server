package com.potatoes.cg.approval.dto.request;

import com.potatoes.cg.approval.domain.Approval;
import com.potatoes.cg.approval.domain.Letter;
import com.potatoes.cg.approval.domain.type.DocumentType;
import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.swing.text.Document;
import java.util.List;


@RequiredArgsConstructor
@Getter
public class LetterCreateRequest {

    private final String documentTitle; // 문서 제목

    private final DocumentType documentType; // 문서 타입

    private final String letterBody; // 품의서 내용

    private final List<Long> approvalLine; // 결재자 맴버코드

    private final List<Long> referenceLine; // 참조자








}


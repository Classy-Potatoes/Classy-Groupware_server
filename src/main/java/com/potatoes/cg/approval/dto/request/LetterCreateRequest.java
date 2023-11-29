package com.potatoes.cg.approval.dto.request;

import com.potatoes.cg.approval.domain.type.DocumentType;
import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.swing.text.Document;
import java.util.List;


@RequiredArgsConstructor
@Getter
public class LetterCreateRequest {


    private final String documentTitle;

    private final DocumentType documentType;

    private final String letterBody;

    private final List<String> approvalLine;

    private final List<Long> referenceLine;







}


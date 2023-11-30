package com.potatoes.cg.approval.dto.request;

import com.potatoes.cg.approval.domain.type.DocumentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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


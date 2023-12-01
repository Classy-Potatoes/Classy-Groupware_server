package com.potatoes.cg.member.dto.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class MemberSearchIdRequest {

    private final Long infoCode;
    private final String infoName;

}

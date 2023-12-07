package com.potatoes.cg.member.dto.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberIdCheckRequest {

    private final Long infoCode;
    private final String infoName;

}

package com.potatoes.cg.member.dto.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberSearchPwdRequest {

    private final String memberId;
    private final Long infoCode;
    private final String infoEmail;

}

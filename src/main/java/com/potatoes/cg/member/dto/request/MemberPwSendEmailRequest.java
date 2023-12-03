package com.potatoes.cg.member.dto.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberPwSendEmailRequest {

    private final String inputMemberId;
    private final Long inputInfoCode;
    private final String inputEmail;

}

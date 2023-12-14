package com.potatoes.cg.member.dto.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberPwdRequest {

    private final String currentPwd;
    private final String memberPassword;

}

package com.potatoes.cg.member.dto.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
//@AllArgsConstructor
@RequiredArgsConstructor
// final 있는 것을 생성자를 만든다.
public class MemberSignupRequest {

    @NotBlank
    private final String memberId;
    @NotBlank
    private final String memberPassword;
    // 이대로 그대로 사용하면 '1234'로 보완에 취약, SecurityConfig 에서 bean 등록
    @NotBlank
    private final String memberName;
    private final String memberEmail;

}

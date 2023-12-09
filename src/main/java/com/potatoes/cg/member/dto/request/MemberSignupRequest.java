package com.potatoes.cg.member.dto.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
@Getter
@RequiredArgsConstructor
public class MemberSignupRequest {

    /* 계정정보 */
    @NotBlank
    private final String memberId;
    @NotBlank
    private final String memberPassword;

    /* 회원정보 */
    private final Long infoCode;
    @NotBlank
    private final String infoEmail;
    @NotBlank
    private final String infoPhone;
    private final Long infoZipcode;
    @NotBlank
    private final String infoAddress;
    @NotBlank
    private final String infoAddressAdd;

}

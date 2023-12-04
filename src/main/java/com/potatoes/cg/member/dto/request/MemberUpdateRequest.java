package com.potatoes.cg.member.dto.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class MemberUpdateRequest {

    @NotBlank
    private final String infoName;
    @Min(value = 1)
    private final Long deptCode;
    @Min(value = 1)
    private final Long jobCode;
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

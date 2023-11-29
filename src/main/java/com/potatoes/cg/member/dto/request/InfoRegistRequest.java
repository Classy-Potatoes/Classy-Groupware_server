package com.potatoes.cg.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class InfoRegistRequest {

    // 사전등록시 필요한것만 NotBlank
    @NotBlank
    private final String infoName;
    @Min(value = 1)
    private final Long jobCode;
    @Min(value = 1)
    private final Long deptCode;

    // 처음 등록시 빈값, 추후 업데이트로 정보 등록
    private final String infoEmail;
    private final String infoPhone;
    private final Long zipCode;
    private final String infoAddress;
    private final String infoAddressAdd;

}

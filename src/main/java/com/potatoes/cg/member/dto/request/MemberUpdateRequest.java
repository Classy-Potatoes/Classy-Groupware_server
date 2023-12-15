package com.potatoes.cg.member.dto.request;


import com.potatoes.cg.member.domain.type.MemberStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class MemberUpdateRequest {

    private final Long infoCode;
    private final String infoPhone;
    private final String infoEmail;
    private final String infoName;
    private final Long deptCode;
    private final Long jobCode;

    private final Long infoZipcode;
    private final String infoAddress;
    private final String infoAddressAdd;

    private final MemberStatus memberStatus;

}

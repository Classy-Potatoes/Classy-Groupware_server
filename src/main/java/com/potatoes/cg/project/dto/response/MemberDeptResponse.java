package com.potatoes.cg.project.dto.response;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.type.MemberStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MemberDeptResponse {

    private final Long infoCode;
    private final String infoName;
    private final Long deptCode;
    private final String deptName;

    public static MemberDeptResponse from(MemberInfo memberInfo, String infoName, Long deptCode, String deptName) {
        return new MemberDeptResponse(
                memberInfo.getInfoCode(),
                infoName,
                deptCode,
                deptName
        );
    }

//    private final Long memberCode;
//    private final MemberStatus memberStatus;

    public static MemberDeptResponse from(final Member member){
        return new MemberDeptResponse(
                member.getMemberInfo().getInfoCode(),
                member.getMemberInfo().getInfoName(),
                member.getMemberInfo().getDept().getDeptCode(),
                member.getMemberInfo().getDept().getDeptName()
        );
    }
}

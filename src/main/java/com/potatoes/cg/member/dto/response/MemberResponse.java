package com.potatoes.cg.member.dto.response;

import com.potatoes.cg.member.domain.Dept;
import com.potatoes.cg.member.domain.Job;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.type.MemberInfoStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MemberResponse {

    private final Long memberCode;
    private final String memberId;
    private final String memberPassword;
    private final String memberStatus;
    private final String memberRole;
    private final LocalDateTime memberJoinDate;
    private final LocalDateTime memberUpdateDate;

    private final Long infoCode;
    private final String infoName;
    private final String jobName;
    private final String deptName;
    private final MemberInfoStatus infoStatus;
    private final String infoEmail;
    private final String infoPhone;
    private final Long infoZipcode;
    private final String infoAddress;
    private final String infoAddressAdd;

    public static MemberResponse from( final Member member ) {

        return new MemberResponse(
                member.getMemberCode(),
                member.getMemberId(),
                member.getMemberPassword(),
                member.getMemberStatus().name(),
                member.getMemberRole().name(),
                member.getMemberJoinDate(),
                member.getMemberUpdateDate(),
                member.getMemberInfo().getInfoCode(),
                member.getMemberInfo().getInfoName(),
                member.getMemberInfo().getJob().getJobName(),
                member.getMemberInfo().getDept().getDeptName(),
                member.getMemberInfo().getInfoStatus(),
                member.getMemberInfo().getInfoEmail(),
                member.getMemberInfo().getInfoPhone(),
                member.getMemberInfo().getInfoZipcode(),
                member.getMemberInfo().getInfoAddress(),
                member.getMemberInfo().getInfoAddressAdd()
        );

    }

}

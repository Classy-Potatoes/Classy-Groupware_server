package com.potatoes.cg.member.dto.response;

import com.potatoes.cg.member.domain.*;
import com.potatoes.cg.member.domain.type.MemberInfoStatus;
import com.potatoes.cg.member.domain.type.MemberStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ProfileResponse {


    private final String memberId;
    private final MemberStatus memberStatus;
    private final Long infoCode;
    private final String infoPhone;
    private final String infoEmail;
    private final String infoName;

    private final Long jobCode;
    private final String jobName;
    private final Long deptCode;
    private final String deptName;
    private final MemberInfoStatus infoStatus;

    private final Long infoZipcode;
    private final String infoAddress;
    private final String infoAddressAdd;

    private final String getFilePathName;

    private final List<Dept> dept;
    private final List<Job> job;
    private final List<History> history;


    public static ProfileResponse from(final Member member, final ProfileImage profileImage, List<Dept> dept, List<Job> job, List<History> history) {

        return new ProfileResponse(
                member.getMemberId(),
                member.getMemberStatus(),
                member.getMemberInfo().getInfoCode(),
                member.getMemberInfo().getInfoPhone(),
                member.getMemberInfo().getInfoEmail(),
                member.getMemberInfo().getInfoName(),
                member.getMemberInfo().getJob().getJobCode(),
                member.getMemberInfo().getJob().getJobName(),
                member.getMemberInfo().getDept().getDeptCode(),
                member.getMemberInfo().getDept().getDeptName(),
                member.getMemberInfo().getInfoStatus(),
                member.getMemberInfo().getInfoZipcode(),
                member.getMemberInfo().getInfoAddress(),
                member.getMemberInfo().getInfoAddressAdd(),
                profileImage.getFilePathName(),
                dept,
                job,
                history
        );

    }

}

package com.potatoes.cg.member.dto.response;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.ProfileImage;
import com.potatoes.cg.member.domain.type.MemberInfoStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ProfileResponse {

//    private final Long infoCode;
    private final String memberId;
    private final String infoPhone;
    private final String infoEmail;
    private final String infoName;

    private final String jobName;
    private final String deptName;
    private final MemberInfoStatus infoStatus;

    private final Long infoZipcode;
    private final String infoAddress;
    private final String infoAddressAdd;

    private final String getFileSaveName;


    public static ProfileResponse from(final Member member, final ProfileImage profileImage) {

        return new ProfileResponse(
                member.getMemberId(),
                member.getMemberInfo().getInfoPhone(),
                member.getMemberInfo().getInfoEmail(),
                member.getMemberInfo().getInfoName(),
                member.getMemberInfo().getJob().getJobName(),
                member.getMemberInfo().getDept().getDeptName(),
                member.getMemberInfo().getInfoStatus(),
                member.getMemberInfo().getInfoZipcode(),
                member.getMemberInfo().getInfoAddress(),
                member.getMemberInfo().getInfoAddressAdd(),
                profileImage.getFileSaveName()
        );

    }

}

package com.potatoes.cg.approval.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
@Getter
@RequiredArgsConstructor
public class AllMemberAndLoginMemberResponse {

        private final LoginUserInfoResponse loginMember;
        private final List<AllMemberResponse> memberList;

}

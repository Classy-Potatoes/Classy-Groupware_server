package com.potatoes.cg.member.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfoModify;
import com.potatoes.cg.member.domain.MemberModify;
import com.potatoes.cg.member.domain.repository.InfoModifyRepository;
import com.potatoes.cg.member.domain.repository.InfoSelectRepository;
import com.potatoes.cg.member.domain.repository.MemberModifyRepository;
import com.potatoes.cg.member.domain.repository.MemberRepository;
import com.potatoes.cg.member.dto.request.MemberSignupRequest;
import com.potatoes.cg.member.dto.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.potatoes.cg.common.exception.type.ExceptionCode.NOT_FOUND_MEMBER_ID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberModifyRepository memberModifyRepository;
    private final PasswordEncoder passwordEncoder;
    private final InfoSelectRepository infoSelectRepository;
    private final InfoModifyRepository infoModifyRepository;

    /* 회원 계정 가입(tbl_member) */
    public void regist( final MemberSignupRequest memberRequest ) {

        final MemberModify newMember = MemberModify.of(
                memberRequest.getMemberId(),
                passwordEncoder.encode( memberRequest.getMemberPassword() ),
                memberRequest.getInfoCode()
        );

        memberModifyRepository.save( newMember );


        // memberInfo table update
        MemberInfoModify info = infoModifyRepository.getReferenceById( memberRequest.getInfoCode() );

        info.update(
                memberRequest.getInfoName(),
                memberRequest.getDeptCode(),
                memberRequest.getJobCode(),
                memberRequest.getInfoEmail(),
                memberRequest.getInfoPhone(),
                memberRequest.getInfoZipcode(),
                memberRequest.getInfoAddress(),
                memberRequest.getInfoAddressAdd()
        );

        // history table insert


    }


    @Transactional(readOnly = true)     // 조회문이기 때문에
    public ProfileResponse getProfile(String memberId ) {

        final Member member = memberRepository.findByMemberId( memberId )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_ID ));

        return ProfileResponse.from( member );

    }



}

package com.potatoes.cg.member.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.member.domain.*;
import com.potatoes.cg.member.domain.repository.*;
import com.potatoes.cg.member.dto.request.MemberSearchIdRequest;
import com.potatoes.cg.member.dto.request.MemberSignupRequest;
import com.potatoes.cg.member.dto.response.MemberResponse;
import com.potatoes.cg.member.dto.response.ProfileResponse;
import com.potatoes.cg.member.dto.response.SearchInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.potatoes.cg.common.exception.type.ExceptionCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final InfoRepository infoRepository;
    private final JobRepository jobRepository;
    private final DeptRepository deptRepository;
    private final HistoryRepository historyRepository;



    /* infoCode(사번) 검증 */
    @Transactional(readOnly = true)
    public SearchInfoResponse infoSearch(Long infoCode) {

        final MemberInfo searchInfo = infoRepository.findById( infoCode )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_INFO_CODE ));

        return SearchInfoResponse.from( searchInfo );

    }


    /* 회원 계정 가입(tbl_member) */
    public void regist( final MemberSignupRequest memberRequest ) {

        MemberInfo info = infoRepository.findById( memberRequest.getInfoCode() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_INFO_CODE ) );

        Dept dept = deptRepository.getReferenceById( memberRequest.getDeptCode() );
        Job job = jobRepository.getReferenceById( memberRequest.getJobCode() );

        info.update(
                memberRequest.getInfoName(),
                dept,
                job,
                memberRequest.getInfoEmail(),
                memberRequest.getInfoPhone(),
                memberRequest.getInfoZipcode(),
                memberRequest.getInfoAddress(),
                memberRequest.getInfoAddressAdd()
        );

        final Member newMember = Member.of(
                memberRequest.getMemberId(),
                passwordEncoder.encode( memberRequest.getMemberPassword() ),
                info
        );

        memberRepository.save( newMember );

    }

    /* 아이디 찾기 */
    public MemberResponse searchId( final MemberSearchIdRequest searchRequest ) {

        final Member member = memberRepository.findByMemberInfoInfoCodeAndMemberInfoInfoName( searchRequest.getInfoCode(), searchRequest.getInfoName() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_INFO_CODE_AND_INFO_NAME ));

        return MemberResponse.from( member );
    }

    /* 아이디 중복 검사 */
    public String duplicateId( final String inputMemberId ) {

        final Member member = memberRepository.findByMemberId( inputMemberId )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_ID ));

//        final Member member = memberRepository.findByMemberId( inputMemberId )
//                .ifPresentOrElse( () -> new NotFoundException( NOT_FOUND_MEMBER_ID ));

        return member.getMemberId();
    }


//    @Transactional(readOnly = true)     // 조회문이기 때문에
//    public ProfileResponse getProfile(String memberId ) {
//
//        final Member member = memberRepository.findByMemberId( memberId )
//                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_ID ));
//
//        return ProfileResponse.from( member );
//
//    }



}

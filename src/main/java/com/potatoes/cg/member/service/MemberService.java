package com.potatoes.cg.member.service;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.repository.MemberRepository;
import com.potatoes.cg.member.dto.request.MemberSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /* 1. 회원 가입 */
    public void regist(final MemberSignupRequest memberRequest) {

//        final Member newMember = Member.of(
//                memberRequest.getMemberId(),
//                // 암호화 인코딩
//                passwordEncoder.encode( memberRequest.getMemberPassword() ),
//                memberRequest.getMemberName(),
//                memberRequest.getMemberEmail()
//        );

//        memberRepository.save( newMember );

    }


//    @Transactional(readOnly = true)     // 조회문이기 때문에
//    public ProfileResponse getProfile( String memberId ) {
//
//        final Member member = memberRepository.findByMemberId( memberId )
//                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_ID ));
//
//        return ProfileResponse.from( member );
//
//    }


}

package com.potatoes.cg.login.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static com.potatoes.cg.common.exception.type.ExceptionCode.*;
import static com.potatoes.cg.member.domain.type.MemberStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
    // 로그인할때 아이디가 있는지 확인하는 서비스

    private final MemberRepository memberRepository;

    // UserDetailsService implements 할때 반드시 만들어야 하는 loadUserByUsername 오버라이드
    @Override
    public UserDetails loadUserByUsername( String memberId ) {

        // Optional 처리 때문에 방식이 달라짐
        Member member = memberRepository.findByMemberId( memberId )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_ID ));

            return User.builder()
                    .username( member.getMemberId() )
                    .password( member.getMemberPassword() )
                    .roles( member.getMemberRole().name() )
                    // 계정 비활성화시 Exception 처리
                    .disabled( !member.getMemberStatus().equals( ACTIVE ) )
                    .build();


    }



}

package com.potatoes.cg.member.domain.repository;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByMemberInfoInfoCodeAndMemberInfoInfoName(Long infoCode, String infoName);

    // null에 대해서 처리하기 위해서 Optional
    Optional<Member> findByMemberId( String memberId );


    // DB에서 refreshToken 조회
    Optional<Member> findByMemberToken(String refreshToken );



}

package com.potatoes.cg.member.domain.repository;

import com.potatoes.cg.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByMemberInfoInfoCodeAndMemberInfoInfoName( Long infoCode, String infoName );

    /* 아이디 중복검사 */
    Boolean existsByMemberId( String memberId );

    /* 아이디찾기(로그인쪽) */
    Optional<Member> findByMemberId( String memberId );

    /* DB에서 refreshToken 조회 */
    Optional<Member> findByMemberToken( String refreshToken );



}

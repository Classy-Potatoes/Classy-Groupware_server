package com.potatoes.cg.member.domain.repository;

import com.potatoes.cg.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // null에 대해서 처리하기 위해서 Optional
    Optional<Member> findByMemberId( String memberId );

    // DB에서 refreshToken 조회
    Optional<Member> findByMemberToken( String refreshToken );


}

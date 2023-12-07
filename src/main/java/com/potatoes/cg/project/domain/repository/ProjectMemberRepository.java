package com.potatoes.cg.project.domain.repository;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.type.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<Member, Long> {
    Page<Member> findByMemberInfoDeptDeptCodeAndMemberStatus(Pageable pageable, Long deptCode, MemberStatus memberStatus);

    Page<Member> findByMemberInfoInfoNameContainsAndMemberStatus(Pageable pageable, String infoName, MemberStatus memberStatus);

    /* 부서별 회원 검색 */
    Page<Member> findByMemberInfoDeptDeptCodeAndMemberInfoInfoNameContainsAndMemberStatus(Pageable pageable, Long deptCode, String infoName, MemberStatus memberStatus);
}

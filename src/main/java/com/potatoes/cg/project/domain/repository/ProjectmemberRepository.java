package com.potatoes.cg.project.domain.repository;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.type.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectmemberRepository extends JpaRepository<Member, Long> {

    /* 부서별 회원 조회 */
    Page<Member> findByMemberInfoDeptDeptCodeAndMemberStatus(Pageable pageable, Long deptCode, MemberStatus memberStatus);

    /* 회원 검색 (초대)*/
    Page<Member> findByMemberInfoInfoNameContainsAndMemberStatus(Pageable pageable, String infoName, MemberStatus memberStatus);
}

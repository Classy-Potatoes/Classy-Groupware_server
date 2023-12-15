package com.potatoes.cg.project.domain.repository;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.type.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<Member, Long> {

    Page<Member> findByMemberInfoInfoNameContainsAndMemberStatus(Pageable pageable, String infoName, MemberStatus memberStatus);

    /* 부서별 회원 검색 */
    List<Member> findByMemberInfoDeptDeptCodeAndMemberInfoInfoNameContainsAndMemberStatus(Long deptCode, String infoName, MemberStatus memberStatus);

    /* 부서별 회원 조회 */
    List<Member> findByMemberInfoDeptDeptCodeAndMemberStatus(Long deptCode, MemberStatus memberStatus);

}

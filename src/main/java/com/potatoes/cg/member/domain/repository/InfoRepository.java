package com.potatoes.cg.member.domain.repository;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.type.MemberInfoStatus;
import com.potatoes.cg.member.domain.type.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/* select 용도 repository */
public interface InfoRepository extends JpaRepository<MemberInfo, Long> {

    /* 미분류 회원 목록 조회 */
    Page<MemberInfo> findByInfoStatus(Pageable pageable, MemberInfoStatus infoStatus);

    /* 미분류 회원 목록 조회(search) */
    Page<MemberInfo> findByInfoNameContainsAndInfoStatus(Pageable pageable, String infoName, MemberInfoStatus infoStatus);

}

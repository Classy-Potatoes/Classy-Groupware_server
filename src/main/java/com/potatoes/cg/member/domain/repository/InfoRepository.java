package com.potatoes.cg.member.domain.repository;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.type.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/* select 용도 repository */
public interface InfoRepository extends JpaRepository<MemberInfo, Long> {


}

package com.potatoes.cg.member.domain.repository;

import com.potatoes.cg.member.domain.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/* select 용도 repository */
public interface InfoRepository extends JpaRepository<MemberInfo, Long> {


}

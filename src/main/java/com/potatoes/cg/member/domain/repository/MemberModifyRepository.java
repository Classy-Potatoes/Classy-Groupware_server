package com.potatoes.cg.member.domain.repository;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberModify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberModifyRepository extends JpaRepository<MemberModify, Long> {



}

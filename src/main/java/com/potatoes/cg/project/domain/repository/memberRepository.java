package com.potatoes.cg.project.domain.repository;

import com.potatoes.cg.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface memberRepository extends JpaRepository<Member, Long> {


}

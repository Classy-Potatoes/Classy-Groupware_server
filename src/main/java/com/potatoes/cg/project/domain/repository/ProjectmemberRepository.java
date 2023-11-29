package com.potatoes.cg.project.domain.repository;

import com.potatoes.cg.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectmemberRepository extends JpaRepository<Member, Long> {


}

package com.potatoes.cg.member.domain.repository;

import com.potatoes.cg.member.domain.MemberInfoSelect;
import org.springframework.data.jpa.repository.JpaRepository;

/* select 용도 repository */
public interface InfoSelectRepository extends JpaRepository<MemberInfoSelect, Long> {


}

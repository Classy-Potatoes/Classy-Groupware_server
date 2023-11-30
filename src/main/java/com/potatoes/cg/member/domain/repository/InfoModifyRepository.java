package com.potatoes.cg.member.domain.repository;

import com.potatoes.cg.member.domain.MemberInfoModify;
import org.springframework.data.jpa.repository.JpaRepository;

/* insert, update 용도 repository */
public interface InfoModifyRepository extends JpaRepository<MemberInfoModify, Long> {


}

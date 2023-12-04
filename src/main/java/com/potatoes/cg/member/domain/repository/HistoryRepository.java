package com.potatoes.cg.member.domain.repository;

import com.potatoes.cg.member.domain.History;
import com.potatoes.cg.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {


    /* 회원이력 조회(마이페이지) */
    Page<History> findByInfoCode(Pageable pageable, Long infoCode);

}

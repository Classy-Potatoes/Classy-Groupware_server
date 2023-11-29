package com.potatoes.cg.approval.domain.repository;

import com.potatoes.cg.approval.domain.Approval;
import com.potatoes.cg.approval.domain.ApprovalLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {



}

package com.potatoes.cg.approval.domain.repository;

import com.potatoes.cg.approval.domain.ApprovalLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Long> {

}

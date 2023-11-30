package com.potatoes.cg.approval.domain.repository;

import com.potatoes.cg.approval.domain.ApprovalFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileEntityRepository extends JpaRepository<ApprovalFile, Long> {



}

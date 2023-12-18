package com.potatoes.cg.approval.domain.repository;

import com.potatoes.cg.approval.domain.Approval;
import com.potatoes.cg.approval.domain.ApprovalLine;
import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineResultType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Long> {

    ApprovalLine findByTurnAndApprovalCode(String nextTurn, Long approvalCode);
}

package com.potatoes.cg.approval.domain.repository;

import com.potatoes.cg.approval.domain.Expense;
import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Expense findByApprovalApprovalCodeAndApprovalDocumentType(Long approvalCode, DocumentType documentType);
}

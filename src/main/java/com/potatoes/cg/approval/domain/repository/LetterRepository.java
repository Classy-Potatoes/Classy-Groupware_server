package com.potatoes.cg.approval.domain.repository;

import com.potatoes.cg.approval.domain.Letter;
import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Long> {

    Letter findByApprovalApprovalCodeAndApprovalDocumentType(Long approvalCode, DocumentType documentType);
}

package com.potatoes.cg.approval.domain.repository;

import com.potatoes.cg.approval.domain.Approval;
import com.potatoes.cg.approval.domain.ApprovalLine;
import com.potatoes.cg.approval.domain.type.approvalType.ApprovalStatusType;
import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {


    /*1. 상신함 게시판 조회  */
    Page<Approval> findByMemberMemberCodeAndApprovalStatus(Pageable pageable, Long memberCode, ApprovalStatusType approvalStatusType);


    @Query("SELECT a.documentType FROM Approval a WHERE a.approvalCode = :approvalCode")
    DocumentType findDocumentTypeValueByApprovalCode(@Param("approvalCode")Long approvalCode);


    Page<Approval> findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndApprovalStatus(Pageable pageable, String documentTitle, LocalDateTime startOfDay, LocalDateTime endOfDay,ApprovalStatusType ApprovalStatus);

    @Modifying
    @Query("UPDATE Approval a SET a.approvalStatus = ?2 WHERE a.approvalCode IN ?1")
    void updateApprovalStatusByApprovalCodeIn(List<Long> approvalCodes, ApprovalStatusType approvalStatusType);
}

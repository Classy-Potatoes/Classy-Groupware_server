package com.potatoes.cg.approval.domain.repository;

import com.potatoes.cg.approval.domain.Approval;
import com.potatoes.cg.approval.domain.ApprovalLine;
import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineResultType;
import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineWaitingStatusType;
import com.potatoes.cg.approval.domain.type.approvalType.ApprovalStatusType;
import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;
import com.potatoes.cg.jwt.CustomUser;
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
    DocumentType findDocumentTypeValueByApprovalCode(@Param("approvalCode") Long approvalCode);


//    Page<Approval> findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndApprovalStatus(Pageable pageable, String documentTitle, LocalDateTime startOfDay, LocalDateTime endOfDay, ApprovalStatus);

    @Modifying
    @Query("UPDATE Approval a SET a.approvalStatus = ?2 WHERE a.approvalCode IN ?1")
    void updateApprovalStatusByApprovalCodeIn(List<Long> approvalCodes, ApprovalStatusType approvalStatusType);


    /* 상신함 검색 내가 작성한 글만 검색 */
    Page<Approval> findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndMemberMemberCodeAndApprovalStatus(Pageable pageable, String documentTitle, LocalDateTime startOfDay, LocalDateTime endOfDay, Long memberCode, ApprovalStatusType approvalStatusType);

    /* 결재함 검색 내가 결재선에 참조 된 글만 검색 */
    Page<Approval> findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndApprovalLineMemberCodeAndApprovalStatus(Pageable pageable, String documentTitle, LocalDateTime startOfDay, LocalDateTime endOfDay, Long memberCode, ApprovalStatusType approvalStatusType);

    /* 결재 대기 상태일때 자신에게 결재요청이 왔을때 상태의 글만 검색 */
    Page<Approval> findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndApprovalLineMemberCodeAndApprovalStatusAndApprovalLineApprovalLineWaitingStatus(Pageable pageable, String documentTitle, LocalDateTime startOfDay, LocalDateTime endOfDay, Long memberCode, ApprovalStatusType approvalStatusType, ApprovalLineWaitingStatusType approvalLineWaitingStatusType);

    /* 내가 결재 할 차례가 왔을때 조회 */
    Page<Approval> findByApprovalLineMemberCodeAndApprovalLineApprovalLineWaitingStatus(Pageable pageable, Long memberCode, ApprovalLineWaitingStatusType approvalLineWaitingStatusType);

    /* 결재 상태에 따라 조회*/
    Page<Approval> findByApprovalLineMemberCodeAndApprovalStatus(Pageable pageable, Long memberCode, ApprovalStatusType approvalStatusType);


    Optional<Approval> findByApprovalCode(Long approvalCode);


    Page<Approval> findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndReferenceLineMemberCode(Pageable pageable, String documentTitle, LocalDateTime startOfDay, LocalDateTime endOfDay, Long memberCode );

    Page<Approval> findByReferenceLineMemberCode(Pageable pageable, Long memberCode);
}

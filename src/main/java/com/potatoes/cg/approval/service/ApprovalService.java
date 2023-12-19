package com.potatoes.cg.approval.service;

import com.potatoes.cg.approval.domain.*;
import com.potatoes.cg.approval.domain.repository.*;
import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineResultType;
import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineWaitingStatusType;
import com.potatoes.cg.approval.domain.type.approvalType.ApprovalStatusType;
import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;
import com.potatoes.cg.approval.dto.request.*;
import com.potatoes.cg.approval.dto.response.*;
import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.common.exception.type.ExceptionCode;
import com.potatoes.cg.common.util.MultipleFileUploadUtils;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalService {


    private final LetterRepository letterRepository;
    private final MemberRepository memberRepository;
    private final ExpenseRepository expenseRepository;
    private final VacationRepository vacationRepository;
    private final ApprovalRepository approvalRepository;
    private final ApprovalLineRepository approvalLineRepository;


    @Value("${file.approval-dir}")
    private String APPROVAL_DIR;


    private String getRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    /* 기안서 작성 - 품의서 */
    @Transactional
    public Long letterSave(final LetterCreateRequest letterRequest, final List<MultipartFile> attachment
            , final CustomUser customUser) {

        /* 전달 된 파일을 서버의 지정 경로에 저장 */
        List<String> replaceFileNames = Collections.emptyList(); //collections.emtyList 불변목록 nullpointer 방지기능
        if (attachment != null && !attachment.isEmpty()) {
            replaceFileNames = MultipleFileUploadUtils.saveFiles(APPROVAL_DIR, (attachment));
        }
        /*파일 정보 저장 */
        List<ApprovalFile> files = new ArrayList<>();
        for (String replaceFileName : replaceFileNames) {
            ApprovalFile fileEntity = new ApprovalFile(
                    replaceFileName,
                    APPROVAL_DIR,
                    getRandomName(),
                    getFileExtension(replaceFileName)

            );
            files.add(fileEntity);
        }


        /* 로그인한 계정 정보 */
        final Member findByLoginmember = memberRepository.findById(customUser.getMemberCode())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER_ID));
        /* 참조자 memberCode  참조자는 안넣을수도 있기때문에 nullpointer 핸들링*/
        final List<Reference> referenceLine =
                Optional.ofNullable(letterRequest.getReferenceLine())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(memberCode -> Reference.of(memberCode))
                        .collect(Collectors.toList());


        /* 결재자 memberCode, 결재 순서 */
        ;
        final List<ApprovalLine> approvalLine = new ArrayList<>();
        for (Map<String, String> approvalLineRequest : letterRequest.getApprovalLine()) {
            String turn = approvalLineRequest.get("turn");
            Long memberCode = Long.parseLong(approvalLineRequest.get("memberCode"));
            ApprovalLine newApprovalLine = ApprovalLine.of(
                    memberCode,
                    turn
            );

            approvalLine.add(newApprovalLine);

        }

        final Approval newApproval = Approval.of(
                letterRequest.getDocumentTitle(),
                referenceLine,
                approvalLine,
                letterRequest.getDocumentType(),
                findByLoginmember,
                files

        );


        final Letter newLetter = Letter.of(letterRequest.getLetterBody(), newApproval);

        final Letter letter = letterRepository.save(newLetter);


        return letter.getApproval().getApprovalCode();
    }

    @Transactional
    public Long expenseSave(ExpenseCreateRequest expenseRequest, List<MultipartFile> attachment, CustomUser customUser) {

        /* 전달 된 파일을 서버의 지정 경로에 저장 */
        List<String> replaceFileNames = Collections.emptyList(); //collections.emtyList 불변목록 nullpointer 방지기능
        if (attachment != null && !attachment.isEmpty()) {
            replaceFileNames = MultipleFileUploadUtils.saveFiles(APPROVAL_DIR, (attachment));
        }
        /*파일 정보 DB 저장 */
        List<ApprovalFile> files = new ArrayList<>();
        for (String replaceFileName : replaceFileNames) {
            ApprovalFile fileEntity = new ApprovalFile(
                    replaceFileName,
                    APPROVAL_DIR,
                    getRandomName(),
                    getFileExtension(replaceFileName)

            );
            files.add(fileEntity);
        }

        /* 로그인한 계정 정보 */
        final Member findByLoginmember = memberRepository.findById(customUser.getMemberCode())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER_ID));

        /* 참조자 memberCode */
        final List<Reference> referenceLine =
                Optional.ofNullable(expenseRequest.getReferenceLine())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(memberCode -> Reference.of(memberCode))
                        .collect(Collectors.toList());

        /* 결재자 memberCode, 결재 순서 */
        ;
        final List<ApprovalLine> approvalLine = new ArrayList<>();
        for (Map<String, String> approvalLineRequest : expenseRequest.getApprovalLine()) {
            String turn = approvalLineRequest.get("turn");
            Long memberCode = Long.parseLong(approvalLineRequest.get("memberCode"));
            ApprovalLine newApprovalLine = ApprovalLine.of(
                    memberCode,
                    turn
            );

            approvalLine.add(newApprovalLine);

        }
        /* expenseDetail 데이터 (계정과목,지출날짜,금액,적요) */
        final List<ExpenseDetail> expenseDetail = new ArrayList<>();
        for (Map<String, String> detailRequest : expenseRequest.getExpenseDetails()) {
            String expenseAccount = detailRequest.get("expenseAccount");
            String expenseBriefs = detailRequest.get("expenseBriefs");
            String expenseDateStr = detailRequest.get("expenseDate");
            LocalDate expenseDate = (expenseDateStr != null) ? LocalDate.parse(expenseDateStr) : null; //null 체크
            String priceStr = detailRequest.get("expensePrice");
            Long expensePrice = (priceStr != null) ? Long.parseLong(priceStr) : null; // null 체크

            ExpenseDetail expenseDetails = ExpenseDetail.of(
                    expenseAccount,
                    expenseDate,
                    expensePrice,
                    expenseBriefs
            );

            expenseDetail.add(expenseDetails);

        }
        final Approval newApproval = Approval.of(
                expenseRequest.getDocumentTitle(),
                referenceLine,
                approvalLine,
                expenseRequest.getDocumentType(),
                findByLoginmember,
                files
        );

        final Expense newExpense = Expense.of(
                expenseRequest.getExpenseNote(),
                expenseRequest.getExpenseStatus(),
                expenseDetail,
                newApproval

        );

        final Expense expense = expenseRepository.save(newExpense);


        return expense.getApproval().getApprovalCode();

    }

    @Transactional
    public Long vacationSave(VacationCreateRequest vacationRequest, CustomUser customUser) {

        /* 로그인한 계정 정보 */
        final Member findByLoginmember = memberRepository.findById(customUser.getMemberCode())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER_ID));

        /* 참조자 memberCode */
        final List<Reference> referenceLine =
                Optional.ofNullable(vacationRequest.getReferenceLine())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(memberCode -> Reference.of(memberCode))
                        .collect(Collectors.toList());

        /* 결재자 memberCode, 결재 순서 */
        ;
        final List<ApprovalLine> approvalLine = new ArrayList<>();
        for (Map<String, String> approvalLineRequest : vacationRequest.getApprovalLine()) {
            String turn = approvalLineRequest.get("turn");
            Long memberCode = Long.parseLong(approvalLineRequest.get("memberCode"));
            ApprovalLine newApprovalLine = ApprovalLine.of(
                    memberCode,
                    turn
            );

            approvalLine.add(newApprovalLine);

        }
        final Approval newApproval = Approval.from(
                vacationRequest.getDocumentTitle(),
                referenceLine,
                approvalLine,
                vacationRequest.getDocumentType(),
                findByLoginmember
        );

        final Vacation newVacation = Vacation.of(
                vacationRequest.getVacationType(),
                vacationRequest.getVacationStartDate(),
                vacationRequest.getVacationEndDate(),
                vacationRequest.getVacationBody(),
                vacationRequest.getVacationEmergencyPhone(),
                newApproval

        );

        final Vacation vacation = vacationRepository.save(newVacation);


        return vacation.getApproval().getApprovalCode();

    }

    /* 1. 로그인 유저 찾아서 품의서, 지출결의서, 휴가신청서에 생성 */
    /* 2. 멤버 전체조회 */
    @Transactional(readOnly = true)
    public AllMemberAndLoginMemberResponse getApprovalProfile(CustomUser customUser) {

        final Member memberInfo = memberRepository.findById(customUser.getMemberCode())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER_ID));

        LoginUserInfoResponse responseForLoginUser = LoginUserInfoResponse.from(memberInfo);


        List<Member> allMembers = memberRepository.findAll();

        List<AllMemberResponse> responsesForAllMembers = allMembers.stream()
                .map(AllMemberResponse::from)
                .collect(Collectors.toList());


        return new AllMemberAndLoginMemberResponse(responseForLoginUser, responsesForAllMembers);
    }

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 7, Sort.by("approvalRegistDate").descending());
    }

    /* 상신함 조회 - 결재대기 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getReport_Waiting(Integer page, CustomUser customUser) {


        Page<Approval> approvalReport = approvalRepository.findByMemberMemberCodeAndApprovalStatus(getPageable(page), customUser.getMemberCode(), ApprovalStatusType.WAITING);

        return approvalReport.map(approval -> ReportResponse.from(approval));
    }

    /* 상신함 조회 - 결재중 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getReport_paying(Integer page, CustomUser customUser) {

        Page<Approval> approvalReport = approvalRepository.findByMemberMemberCodeAndApprovalStatus(getPageable(page), customUser.getMemberCode(), ApprovalStatusType.PAYING);

        return approvalReport.map(approval -> ReportResponse.from(approval));
    }

    /* 상신함 조회 - 승인 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getReport_approve(Integer page, CustomUser customUser) {
        Page<Approval> approvalReport = approvalRepository.findByMemberMemberCodeAndApprovalStatus(getPageable(page), customUser.getMemberCode(), ApprovalStatusType.APPROVE);

        return approvalReport.map(approval -> ReportResponse.from(approval));
    }

    /* 상신함 조회 - 반려 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getReport_turnback(Integer page, CustomUser customUser) {
        Page<Approval> approvalReport = approvalRepository.findByMemberMemberCodeAndApprovalStatus(getPageable(page), customUser.getMemberCode(), ApprovalStatusType.TURNBACK);

        return approvalReport.map(approval -> ReportResponse.from(approval));
    }

    /* 상신함 조회 - 회수 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getReport_recall(Integer page, CustomUser customUser) {
        Page<Approval> approvalReport = approvalRepository.findByMemberMemberCodeAndApprovalStatus(getPageable(page), customUser.getMemberCode(), ApprovalStatusType.RECALL);

        return approvalReport.map(approval -> ReportResponse.from(approval));
    }

    /* 기안서 양식이 무엇인지에 따라 상세 페이지 조회 */
    @Transactional(readOnly = true)
    public DocumentType getDocumentType(Long approvalCode) {

        return approvalRepository.findDocumentTypeValueByApprovalCode(approvalCode);
    }

    /* 상신함 상세페이지 - 품의서 */
    @Transactional(readOnly = true)
    public ReportDetail_LetterResponse getLetterDetail(Long approvalCode, CustomUser customUser) {


        Letter approvalLetter = letterRepository.findByApprovalApprovalCodeAndApprovalDocumentType(approvalCode, DocumentType.LETTER);

        Member loginMember = memberRepository.findById(customUser.getMemberCode())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER_ID));

        return ReportDetail_LetterResponse.from(approvalLetter, loginMember);
    }

    /* 상신함 상세페이지 - 휴가신청서*/
    @Transactional(readOnly = true)
    public ReportDetail_VacationResponse getVacationDetail(Long approvalCode, CustomUser customUser) {

        Vacation approvalVacation = vacationRepository.findByApprovalApprovalCodeAndApprovalDocumentType(approvalCode, DocumentType.VACATION);

        Member loginMember = memberRepository.findById(customUser.getMemberCode())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER_ID));

        return ReportDetail_VacationResponse.from(approvalVacation, loginMember);


    }

    /* 상신함 상세페이지 - 지출결의서*/
    @Transactional(readOnly = true)
    public ReportDetail_ExpenseResponse getExpenseDetail(Long approvalCode, CustomUser customUser) {

        Expense approvalExpense = expenseRepository.findByApprovalApprovalCodeAndApprovalDocumentType(approvalCode, DocumentType.EXPENSE);

        Member loginMember = memberRepository.findById(customUser.getMemberCode())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER_ID));

        return ReportDetail_ExpenseResponse.from(approvalExpense, loginMember);

    }

    /* 상신함 - 결재대기 검색 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getSearchReport(Integer page, String documentTitle, LocalDate startDate, LocalDate endDate, CustomUser customUser) {

        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59);

        Page<Approval> approvals = approvalRepository.findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndMemberMemberCodeAndApprovalStatus(getPageable(page), documentTitle, startOfDay, endOfDay, customUser.getMemberCode(), ApprovalStatusType.WAITING);

        return approvals.map(approval -> ReportResponse.from(approval));

    }

    /* 상신함 - 결재중 검색 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getSearchPayingReport(Integer page, String documentTitle, LocalDate startDate, LocalDate endDate, CustomUser customUser) {

        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59);

        Page<Approval> approvals = approvalRepository.findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndMemberMemberCodeAndApprovalStatus(getPageable(page), documentTitle, startOfDay, endOfDay, customUser.getMemberCode(), ApprovalStatusType.PAYING);

        return approvals.map(approval -> ReportResponse.from(approval));
    }

    /* 상신함 - 승인 검색 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getSearchApproveReport(Integer page, String documentTitle, LocalDate startDate, LocalDate endDate, CustomUser customUser) {
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59);

        Page<Approval> approvals = approvalRepository.findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndMemberMemberCodeAndApprovalStatus(getPageable(page), documentTitle, startOfDay, endOfDay, customUser.getMemberCode(), ApprovalStatusType.APPROVE);

        return approvals.map(approval -> ReportResponse.from(approval));
    }

    /* 상신함 - 반려 검색 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getSearchTurnbackReport(Integer page, String documentTitle, LocalDate startDate, LocalDate endDate, CustomUser customUser) {
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59);

        Page<Approval> approvals = approvalRepository.findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndMemberMemberCodeAndApprovalStatus(getPageable(page), documentTitle, startOfDay, endOfDay, customUser.getMemberCode(), ApprovalStatusType.TURNBACK);

        return approvals.map(approval -> ReportResponse.from(approval));
    }

    /* 상신함 - 회수 검색 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getSearchRecallReport(Integer page, String documentTitle, LocalDate startDate, LocalDate endDate, CustomUser customUser) {
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59);

        Page<Approval> approvals = approvalRepository.findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndMemberMemberCodeAndApprovalStatus(getPageable(page), documentTitle, startOfDay, endOfDay, customUser.getMemberCode(), ApprovalStatusType.RECALL);

        return approvals.map(approval -> ReportResponse.from(approval));
    }


    /* 회수 처리 */
    @Transactional
    public void reportApprovalStatusTypeUpdate(ReportApprovalStatusUpdateRequest approvalCode) {

        List<Long> approvalCodes = approvalCode.getApprovalCode();

        approvalRepository.updateApprovalStatusByApprovalCodeIn(approvalCodes, ApprovalStatusType.RECALL);


    }

    /* 결재함 게시판 - 결재 대기 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getSign_Waiting(Integer page, CustomUser customUser) {

        Page<Approval> approvalSign = approvalRepository.findByApprovalLineMemberCodeAndApprovalLineApprovalLineWaitingStatusAndApprovalStatusNot(
                getPageable(page),
                customUser.getMemberCode(),
                ApprovalLineWaitingStatusType.REQUEST,
                ApprovalStatusType.RECALL);

        return approvalSign.map(approval -> ReportResponse.from(approval));
    }

    /* 결재함 게시판 - 결재중 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getSign_Paying(Integer page, CustomUser customUser) {
        Page<Approval> approvalSign = approvalRepository.findByApprovalLineMemberCodeAndApprovalStatus(
                getPageable(page),
                customUser.getMemberCode(),
                ApprovalStatusType.PAYING);

        return approvalSign.map(approval -> ReportResponse.from(approval));
    }

    /* 결재함 게시판 - 승인 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getSign_Approve(Integer page, CustomUser customUser) {
        Page<Approval> approvalSign = approvalRepository.findByApprovalLineMemberCodeAndApprovalStatus(
                getPageable(page),
                customUser.getMemberCode(),
                ApprovalStatusType.APPROVE);

        return approvalSign.map(approval -> ReportResponse.from(approval));
    }

    /* 결재함 게시판 - 반려 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getSign_Turnback(Integer page, CustomUser customUser) {
        Page<Approval> approvalSign = approvalRepository.findByApprovalLineMemberCodeAndApprovalStatus(
                getPageable(page),
                customUser.getMemberCode(),
                ApprovalStatusType.TURNBACK);

        return approvalSign.map(approval -> ReportResponse.from(approval));
    }

    /* 결재함 검색 - 결재대기 - 내게 참조되고 결재요청이 내 차례일때 결재만 검색 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> searchSign_Waiting(Integer page, String documentTitle, LocalDate startDate, LocalDate endDate, CustomUser customUser) {

        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59);

        Page<Approval> approvals = approvalRepository.findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndApprovalLineMemberCodeAndApprovalStatusAndApprovalLineApprovalLineWaitingStatus(
                getPageable(page),
                documentTitle,
                startOfDay,
                endOfDay,
                customUser.getMemberCode(),
                ApprovalStatusType.WAITING,
                ApprovalLineWaitingStatusType.REQUEST);

        return approvals.map(approval -> ReportResponse.from(approval));
    }

    /* 결재함 검색 - 결재중 - 내게 참조 된 결재만 검색 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> searchSign_paying(Integer page, String documentTitle, LocalDate startDate, LocalDate endDate, CustomUser customUser) {
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59);

        Page<Approval> approvals = approvalRepository
                .findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndApprovalLineMemberCodeAndApprovalStatus(
                        getPageable(page),
                        documentTitle,
                        startOfDay,
                        endOfDay,
                        customUser.getMemberCode(),
                        ApprovalStatusType.PAYING);

        return approvals.map(approval -> ReportResponse.from(approval));
    }

    /* 결재함 검색 - 승인 - 내게 참조 된 결재만 검색 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> searchSign_approve(Integer page, String documentTitle, LocalDate startDate, LocalDate endDate, CustomUser customUser) {
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59);

        Page<Approval> approvals = approvalRepository
                .findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndApprovalLineMemberCodeAndApprovalStatus(
                        getPageable(page),
                        documentTitle,
                        startOfDay,
                        endOfDay,
                        customUser.getMemberCode(),
                        ApprovalStatusType.APPROVE);

        return approvals.map(approval -> ReportResponse.from(approval));
    }

    /* 결재함 검색 - 반려 - 내게 참조 된 결재만 검색 */
    @Transactional(readOnly = true)
    public Page<ReportResponse> searchSign_turnback(Integer page, String documentTitle, LocalDate startDate, LocalDate endDate, CustomUser customUser) {
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59);

        Page<Approval> approvals = approvalRepository
                .findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndApprovalLineMemberCodeAndApprovalStatus(
                        getPageable(page),
                        documentTitle,
                        startOfDay,
                        endOfDay,
                        customUser.getMemberCode(),
                        ApprovalStatusType.TURNBACK);

        return approvals.map(approval -> ReportResponse.from(approval));
    }


    /* 결재자 결재 처리 */
    @Transactional
    public void signUp(SignRequest signRequest, Long approvalCode, CustomUser customUser) {

        Approval existingApproval = approvalRepository
                .findById(approvalCode)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_APPROVAL_CODE));


        List<ApprovalLine> approvalLines = existingApproval.getApprovalLine();

        ApprovalLine existingApprovalLine = null;
        ApprovalLine nextApprovalLine = null;
        for (int i = 0; i < approvalLines.size(); i++) {
            if (approvalLines.get(i).getMemberCode().equals(customUser.getMemberCode())) {
                existingApprovalLine = approvalLines.get(i);
                if (i + 1 < approvalLines.size()) {
                    nextApprovalLine = approvalLines.get(i + 1);
                }
                break;
            }
        }

        existingApproval.setApprovalTurnbackReason(signRequest.getApprovalTurnbackReson());

        existingApprovalLine.setApprovalLineResult(signRequest.getApprovalLineResult());

        if ("TURNBACK".equals(existingApprovalLine.getApprovalLineResult())) {
            existingApproval.setApprovalStatus(ApprovalStatusType.TURNBACK);
            existingApproval.setApprovalTurnbackDate(LocalDateTime.now());
        }

        existingApprovalLine.setApprovalLineWaitingStatus(ApprovalLineWaitingStatusType.COMPLETE);
        existingApprovalLine.setApprovalLineDate(LocalDateTime.now());


        if ("1".equals(existingApprovalLine.getTurn())) {
            // 문서 진행중으로 변경
                existingApproval.setApprovalStatus(ApprovalStatusType.PAYING);
            }


            if (nextApprovalLine == null) {
                // 전체 문서 완료
                if ("TURNBACK".equals(existingApprovalLine.getApprovalLineResult())) {
                    existingApproval.setApprovalStatus(ApprovalStatusType.TURNBACK);
                    existingApproval.setApprovalTurnbackDate(LocalDateTime.now());
                    existingApproval.setApprovalTurnbackReason(signRequest.getApprovalTurnbackReson());
                } else if ("APPROVE".equals(existingApprovalLine.getApprovalLineResult())) {
                    existingApproval.setApprovalStatus(ApprovalStatusType.APPROVE);
                    existingApproval.setApprovalApproveDate(LocalDateTime.now());
                }
            } else {
                // nextline 처리
                if ("TURNBACK".equals(existingApprovalLine.getApprovalLineResult())){
                    existingApproval.setApprovalStatus(ApprovalStatusType.TURNBACK);
                    nextApprovalLine.setApprovalLineWaitingStatus(ApprovalLineWaitingStatusType.CANCEL);
                } else if ("APPROVE".equals(existingApprovalLine.getApprovalLineResult())) {
                    nextApprovalLine.setApprovalLineWaitingStatus(ApprovalLineWaitingStatusType.REQUEST);
                }
            }


            approvalLineRepository.save(existingApprovalLine);
            approvalRepository.save(existingApproval);

        }


        /* 참조 보관함 검색 조회  */
    @Transactional(readOnly = true)
    public Page<ReportResponse> searchReferenceReport(Integer page, String documentTitle, LocalDate startDate, LocalDate endDate, CustomUser customUser) {

        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59);

        Page<Approval> approvals = approvalRepository
                .findByDocumentTitleIgnoreCaseContainingAndApprovalRegistDateBetweenAndReferenceLineMemberCode(
                        getPageable(page),
                        documentTitle,
                        startOfDay,
                        endOfDay,
                        customUser.getMemberCode()
                        );

        return approvals.map(approval -> ReportResponse.from(approval));
    }

    /* 참조 보관함 조회 */
    public Page<ReportResponse> getReferenceReport(Integer page, CustomUser customUser) {
        Page<Approval> approvalReport = approvalRepository.findByReferenceLineMemberCode(getPageable(page), customUser.getMemberCode());

        return approvalReport.map(approval -> ReportResponse.from(approval));
    }
}

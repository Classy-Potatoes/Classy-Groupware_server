package com.potatoes.cg.approval.service;

import com.potatoes.cg.approval.domain.*;
import com.potatoes.cg.approval.domain.repository.*;
import com.potatoes.cg.approval.domain.type.approvalLineType.ApprovalLineTurnType;
import com.potatoes.cg.approval.dto.request.ExpenseCreateRequest;
import com.potatoes.cg.approval.dto.request.LetterCreateRequest;
import com.potatoes.cg.approval.dto.request.VacationCreateRequest;
import com.potatoes.cg.approval.dto.response.AllMemberAndLoginMemberResponse;
import com.potatoes.cg.approval.dto.response.AllMemberResponse;
import com.potatoes.cg.approval.dto.response.LoginUserInfoResponse;
import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.common.exception.type.ExceptionCode;
import com.potatoes.cg.common.util.MultipleFileUploadUtils;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.member.domain.repository.MemberRepository;
import com.potatoes.cg.member.dto.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final InfoRepository infoRepository;


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


        /* 결재자 memberCode, 결재 순서 */;
        final List<ApprovalLine> approvalLine = new ArrayList<>();
        for (Map<String, String> approvalLineRequest : letterRequest.getApprovalLine()) {
            String turn = approvalLineRequest.get("turn");
            Long memberCode = Long.parseLong(approvalLineRequest.get("memberCode"));
            ApprovalLine newApprovalLine  = ApprovalLine.of(
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

        /* 결재자 memberCode, 결재 순서 */;
        final List<ApprovalLine> approvalLine = new ArrayList<>();
        for (Map<String, String> approvalLineRequest : expenseRequest.getApprovalLine()) {
            String turn = approvalLineRequest.get("turn");
            Long memberCode = Long.parseLong(approvalLineRequest.get("memberCode"));
            ApprovalLine newApprovalLine  = ApprovalLine.of(
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

        /* 결재자 memberCode, 결재 순서 */;
        final List<ApprovalLine> approvalLine = new ArrayList<>();
        for (Map<String, String> approvalLineRequest : vacationRequest.getApprovalLine()) {
            String turn = approvalLineRequest.get("turn");
            Long memberCode = Long.parseLong(approvalLineRequest.get("memberCode"));
            ApprovalLine newApprovalLine  = ApprovalLine.of(
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
    /* 2. 멤버 전체조회*/
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

}
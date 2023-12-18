
package com.potatoes.cg.approval.presentation;

import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;
import com.potatoes.cg.approval.dto.request.*;
import com.potatoes.cg.approval.dto.response.*;
import com.potatoes.cg.approval.service.ApprovalService;
import com.potatoes.cg.common.paging.Pagenation;
import com.potatoes.cg.common.paging.PagingButtonInfo;
import com.potatoes.cg.common.paging.PagingResponse;
import com.potatoes.cg.jwt.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cg-api/v1/approval")
@Slf4j
public class ApprovalController {

    private final ApprovalService approvalService;

    /* 기안서 작성 - 품의서  */
    @PostMapping("/letter")
    public ResponseEntity<Void> letterSave(@RequestPart(required = false) @Valid final LetterCreateRequest letterRequest,
                                           @AuthenticationPrincipal CustomUser customUser,
                                           @RequestPart(required = false) final List<MultipartFile> attachment) {


        final Long letterCode = approvalService.letterSave(letterRequest, attachment, customUser);


        return ResponseEntity.created(URI.create("/letter/" + letterCode)).build();
    }
    /* 기안서 작성 - 품의서 로그인 사용자 정보 조회 + 결재자를 위해 맴버 전체 조회*/
    @GetMapping(value = {"/letter","/expense","/vacation"})
    public ResponseEntity<AllMemberAndLoginMemberResponse> approvalProfile(@AuthenticationPrincipal CustomUser user) {

        final AllMemberAndLoginMemberResponse memberInfoResponse = approvalService.getApprovalProfile(user);

        return ResponseEntity.ok(memberInfoResponse);
    }

    /* 기안서 작성 - 지출결의서 */
    @PostMapping("/expense")
    public ResponseEntity<Void> expenseSave(@RequestPart(required = false) @Valid final ExpenseCreateRequest expenseRequest,
                                            @AuthenticationPrincipal CustomUser customUser,
                                            @RequestPart(required = false) final List<MultipartFile> attachment) {



        final Long expenseCode = approvalService.expenseSave(expenseRequest, attachment , customUser);

        return ResponseEntity.created(URI.create("/expense/" + expenseCode)).build();


    }

    /* 기안서 작성 - 휴가 신청서 */
    @PostMapping("/vacation")
    public ResponseEntity<Void> vacationSave(@RequestBody(required = false) @Valid final VacationCreateRequest vacationRequest,
                                             @AuthenticationPrincipal CustomUser customUser) {

        final Long vacationCode = approvalService.vacationSave(vacationRequest,customUser);

        return ResponseEntity.created(URI.create("/vacation/"+vacationCode)).build();
    }


    /*상신함 조회- 결재대기 */
    @GetMapping("/report-waiting")
    public ResponseEntity<PagingResponse> getReport_Waiting(@AuthenticationPrincipal final CustomUser customUser,
                                                            @RequestParam(defaultValue = "1") final  Integer page) {

        final Page<ReportResponse> report = approvalService.getReport_Waiting(page,customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }
    /*상신함 조회- 결재중 */
    @GetMapping("/report-paying")
    public ResponseEntity<PagingResponse> getReport_paying(@AuthenticationPrincipal final CustomUser customUser,
                                                           @RequestParam(defaultValue = "1") final  Integer page) {

        final Page<ReportResponse> report = approvalService.getReport_paying(page,customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }
    /*상신함 조회- 승인 */
    @GetMapping("/report-approve")
    public ResponseEntity<PagingResponse> getReport_approve(@AuthenticationPrincipal final CustomUser customUser,
                                                            @RequestParam(defaultValue = "1") final  Integer page) {

        final Page<ReportResponse> report = approvalService.getReport_approve(page,customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /*상신함 조회- 반려 */
    @GetMapping("/report-turnback")
    public ResponseEntity<PagingResponse> getReport_turnback(@AuthenticationPrincipal final CustomUser customUser,
                                                             @RequestParam(defaultValue = "1") final  Integer page) {

        final Page<ReportResponse> report = approvalService.getReport_turnback(page,customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /*상신함 조회- 회수 */
    @GetMapping("/report-recall")
    public ResponseEntity<PagingResponse> getReport_recall(@AuthenticationPrincipal final CustomUser customUser,
                                                           @RequestParam(defaultValue = "1") final  Integer page) {

        final Page<ReportResponse> report = approvalService.getReport_recall(page,customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /*상신함 상세페이지  */
    @GetMapping("/report/{approvalCode}")
    public ResponseEntity<?> getReportDetail(@PathVariable final Long approvalCode,
                                             @AuthenticationPrincipal final CustomUser customUser){

        DocumentType documentType = approvalService.getDocumentType(approvalCode);

        switch (documentType) {
            case LETTER:
                ReportDetail_LetterResponse letterResponse = approvalService.getLetterDetail(approvalCode, customUser);
                return ResponseEntity.ok(letterResponse);
            case VACATION:
                ReportDetail_VacationResponse vacationResponse = approvalService.getVacationDetail(approvalCode, customUser);
                return ResponseEntity.ok(vacationResponse);
            case EXPENSE:
                ReportDetail_ExpenseResponse expenseResponse = approvalService.getExpenseDetail(approvalCode, customUser);
                return ResponseEntity.ok(expenseResponse);
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 문서를 찾을 수 없습니다.");
        }
    }
    /* 상신함 검색 - 결재대기 */
    @GetMapping("/report/search-waiting")
    public ResponseEntity<PagingResponse> searchReport(@RequestParam(defaultValue = "1") final Integer page,
                                                       @RequestParam(required = false) final  String documentTitle,
                                                       @RequestParam(required = false) final @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                       @RequestParam(required = false) final  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                       @AuthenticationPrincipal CustomUser customUser) {

        final Page<ReportResponse> report = approvalService.getSearchReport(page,documentTitle,startDate,endDate, customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);


        return ResponseEntity.ok(pagingResponse);
    }
    /* 상신함 검색 - 결재중 */
    @GetMapping("/report/search-paying")
    public ResponseEntity<PagingResponse> searchPayingReport(@RequestParam(defaultValue = "1") final Integer page,
                                                       @RequestParam(required = false) final  String documentTitle,
                                                       @RequestParam(required = false) final @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                       @RequestParam(required = false) final  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                             @AuthenticationPrincipal CustomUser customUser) {

        final Page<ReportResponse> report = approvalService.getSearchPayingReport(page,documentTitle,startDate,endDate,customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);


        return ResponseEntity.ok(pagingResponse);
    }
    /* 상신함, 결재함 검색 - 승인 */
    @GetMapping("/report/search-approve")
    public ResponseEntity<PagingResponse> searchApproveReport(@RequestParam(defaultValue = "1") final Integer page,
                                                             @RequestParam(required = false) final  String documentTitle,
                                                             @RequestParam(required = false) final @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                             @RequestParam(required = false) final  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                              @AuthenticationPrincipal CustomUser customUser) {

        final Page<ReportResponse> report = approvalService.getSearchApproveReport(page,documentTitle,startDate,endDate,customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);


        return ResponseEntity.ok(pagingResponse);
    }
    /* 상신함, 결재함 검색 - 반려 */
    @GetMapping("/report/search-turnback")
    public ResponseEntity<PagingResponse> searchTurnbackReport(@RequestParam(defaultValue = "1") final Integer page,
                                                              @RequestParam(required = false) final  String documentTitle,
                                                              @RequestParam(required = false) final @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                              @RequestParam(required = false) final  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                               @AuthenticationPrincipal CustomUser customUser) {

        final Page<ReportResponse> report = approvalService.getSearchTurnbackReport(page,documentTitle,startDate,endDate, customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);


        return ResponseEntity.ok(pagingResponse);
    }
    /* 상신함 검색 - 회수  */
    @GetMapping("/report/search-recall")
    public ResponseEntity<PagingResponse> searchRecallReport(@RequestParam(defaultValue = "1") final Integer page,
                                                              @RequestParam(required = false) final  String documentTitle,
                                                              @RequestParam(required = false) final @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                              @RequestParam(required = false) final  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                             @AuthenticationPrincipal CustomUser customUser) {

        final Page<ReportResponse> report = approvalService.getSearchRecallReport(page,documentTitle,startDate,endDate,customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);


        return ResponseEntity.ok(pagingResponse);
    }

    /* 상신함 회수 처리 */
    @PutMapping("/report-waiting")
    public ResponseEntity<Void> reportApprovalStatusUpdate(@RequestBody final ReportApprovalStatusUpdateRequest approvalCode) {

        approvalService.reportApprovalStatusTypeUpdate(approvalCode);

        return ResponseEntity.created(URI.create("/report-waiting")).build();
    }

    /*결재함 - 결재대기 */
    @GetMapping("/sign-waiting")
    public ResponseEntity<PagingResponse> getSign_Waiting(@AuthenticationPrincipal final CustomUser customUser,
                                                            @RequestParam(defaultValue = "1") final  Integer page) {

        final Page<ReportResponse> sign = approvalService.getSign_Waiting(page,customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(sign);
        final PagingResponse pagingResponse = PagingResponse.of(sign.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /*결재함 - 결재중 */
    @GetMapping("/sign-paying")
    public ResponseEntity<PagingResponse> getSign_Paying(@AuthenticationPrincipal final CustomUser customUser,
                                                          @RequestParam(defaultValue = "1") final  Integer page) {

        final Page<ReportResponse> sign = approvalService.getSign_Paying(page,customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(sign);
        final PagingResponse pagingResponse = PagingResponse.of(sign.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /*결재함 - 승인 */
    @GetMapping("/sign-approve")
    public ResponseEntity<PagingResponse> getSign_Approve(@AuthenticationPrincipal final CustomUser customUser,
                                                         @RequestParam(defaultValue = "1") final  Integer page) {

        final Page<ReportResponse> sign = approvalService.getSign_Approve(page,customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(sign);
        final PagingResponse pagingResponse = PagingResponse.of(sign.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /*결재함 - 반려 */
    @GetMapping("/sign-turnback")
    public ResponseEntity<PagingResponse> getSign_Turnback(@AuthenticationPrincipal final CustomUser customUser,
                                                         @RequestParam(defaultValue = "1") final  Integer page) {

        final Page<ReportResponse> sign = approvalService.getSign_Turnback(page,customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(sign);
        final PagingResponse pagingResponse = PagingResponse.of(sign.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /* 결재함 검색 - 결재 대기 : 내게 결재선이 올라와 있는 문서들만 조회*/
    @GetMapping("/sign/search-waiting")
    public ResponseEntity<PagingResponse> searchSign_Waiting(@RequestParam(defaultValue = "1") final Integer page,
                                                       @RequestParam(required = false) final  String documentTitle,
                                                       @RequestParam(required = false) final @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                       @RequestParam(required = false) final  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                       @AuthenticationPrincipal CustomUser customUser) {

        final Page<ReportResponse> report = approvalService.searchSign_Waiting(page,documentTitle,startDate,endDate, customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);


        return ResponseEntity.ok(pagingResponse);
    }
    /* 결재함 검색 - 결재중 */
    @GetMapping("/sign/search-paying")
    public ResponseEntity<PagingResponse> searchSign_Paying(@RequestParam(defaultValue = "1") final Integer page,
                                                             @RequestParam(required = false) final  String documentTitle,
                                                             @RequestParam(required = false) final @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                             @RequestParam(required = false) final  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                             @AuthenticationPrincipal CustomUser customUser) {

        final Page<ReportResponse> report = approvalService.searchSign_paying(page,documentTitle,startDate,endDate, customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);


        return ResponseEntity.ok(pagingResponse);
    }
    /* 결재함 검색 - 승인 */
    @GetMapping("/sign/search-approve")
    public ResponseEntity<PagingResponse> searchSign_Approve(@RequestParam(defaultValue = "1") final Integer page,
                                                             @RequestParam(required = false) final  String documentTitle,
                                                             @RequestParam(required = false) final @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                             @RequestParam(required = false) final  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                             @AuthenticationPrincipal CustomUser customUser) {

        final Page<ReportResponse> report = approvalService.searchSign_approve(page,documentTitle,startDate,endDate, customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);


        return ResponseEntity.ok(pagingResponse);
    }

    /* 결재함 검색 - 반려 */
    @GetMapping("/sign/search-turnback")
    public ResponseEntity<PagingResponse> searchSign_Turnback(@RequestParam(defaultValue = "1") final Integer page,
                                                             @RequestParam(required = false) final  String documentTitle,
                                                             @RequestParam(required = false) final @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                             @RequestParam(required = false) final  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                             @AuthenticationPrincipal CustomUser customUser) {

        final Page<ReportResponse> report = approvalService.searchSign_turnback(page,documentTitle,startDate,endDate, customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);


        return ResponseEntity.ok(pagingResponse);
    }
    /* 결재자 결재 */
    @PutMapping("/report/{approvalCode}")
    public ResponseEntity<Void> sign(@RequestBody final SignRequest signRequest,
                                     @PathVariable final Long approvalCode,
                                     @AuthenticationPrincipal final CustomUser customUser
                                     ) {

        approvalService.signUp(signRequest,approvalCode, customUser);

        return ResponseEntity.created(URI.create("/signup")).build();

    }

    /* 참조보관함 */
    @GetMapping("/report/search-reference")
    public ResponseEntity<PagingResponse> referenceSearchReport(@RequestParam(defaultValue = "1") final Integer page,
                                                              @RequestParam(required = false) final  String documentTitle,
                                                              @RequestParam(required = false) final @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                              @RequestParam(required = false) final  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                              @AuthenticationPrincipal CustomUser customUser) {

        final Page<ReportResponse> referenceReport = approvalService.searchReferenceReport(page,documentTitle,startDate,endDate, customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(referenceReport);
        final PagingResponse pagingResponse = PagingResponse.of(referenceReport.getContent(), pagingButtonInfo);


        return ResponseEntity.ok(pagingResponse);
    }

    /*상신함 조회- 결재중 */
    @GetMapping("/report/reference")
    public ResponseEntity<PagingResponse> referenceReport(@AuthenticationPrincipal final CustomUser customUser,
                                                           @RequestParam(defaultValue = "1") final  Integer page) {

        final Page<ReportResponse> report = approvalService.getReferenceReport(page,customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(report);
        final PagingResponse pagingResponse = PagingResponse.of(report.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }




}





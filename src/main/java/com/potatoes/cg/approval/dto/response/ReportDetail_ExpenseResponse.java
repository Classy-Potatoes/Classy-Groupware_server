package com.potatoes.cg.approval.dto.response;

import com.potatoes.cg.approval.domain.*;
import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;
import com.potatoes.cg.approval.domain.type.expenseType.ExpenseStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ReportDetail_ExpenseResponse {

    private final String jobName;
    private final String deptName;
    private final String infoName;
    private final List<Map<String,Object>> approvalLine;
    private final List<Map<String,Object>> referenceLine;
    private final String documentTitle;
    private final LocalDateTime approvalRegistDate;
    private final String approvalTurnbackReason;
    private final List<Map<String,Object>> attachment;
    private final DocumentType documentType;

    /*지출 결의서 정보*/
    private final String expenseNote;
    private final ExpenseStatusType expenseStatus;
    private final List<Map<String,Object>> expenseDetails;


    public static ReportDetail_ExpenseResponse from(Expense approvalExpense) {
        /* 결재선에 필요한 정보만 가져오기 */
        List<ApprovalLine_GET> approvalExpenseLines = approvalExpense.getApproval().getApprovalLine_GET();

        List<Map<String, Object>> transformedApprovalLines = approvalExpenseLines.stream()
                .map(approvalLine -> {
                    Map<String, Object> transformedLine = new HashMap<>();
                    transformedLine.put("memberCode", approvalLine.getMember().getMemberCode());
                    transformedLine.put("turn", approvalLine.getTurn());
                    String infoName = approvalLine.getMember().getMemberInfo().getInfoName();
                    transformedLine.put("infoName", infoName);
                    transformedLine.put("approvalLineResult", approvalLine.getApprovalLineResult());
                    transformedLine.put("approvalLineWaitingStatus", approvalLine.getApprovalLineWaitingStatus());
                    transformedLine.put("approvalLineDate", approvalLine.getApprovalLineDate());
                    return transformedLine;
                })
                .collect(Collectors.toList());

        /* 참조선 필요한 정보 가져오기 */
        List<ReferenceLine_GET> referenceLines = approvalExpense.getApproval().getReferenceLine_GET();

        List<Map<String,Object>> transformedReferenceLines = referenceLines.stream()
                .map(referenceLine -> {
                    Map<String, Object> transformedLine = new HashMap<>();
                    transformedLine.put("memberCode", referenceLine.getMember().getMemberCode());
                    String infoName = referenceLine.getMember().getMemberInfo().getInfoName();
                    transformedLine.put("infoName", infoName);
                    String jobName = referenceLine.getMember().getMemberInfo().getJob().getJobName();
                    transformedLine.put("jobName", jobName);
                    String deptName = referenceLine.getMember().getMemberInfo().getDept().getDeptName();
                    transformedLine.put("deptName", deptName);
                    return transformedLine;
                })
                .collect(Collectors.toList());

        List<ApprovalFile> approvalFiles = approvalExpense.getApproval().getAttachment();

        List<Map<String,Object>> transformedFiles = approvalFiles.stream()
                .map(approvalFile -> {
                    Map<String, Object> transformedFile = new HashMap<>();
                    transformedFile.put("fileName", approvalFile.getFileName());
                    transformedFile.put("filePathName", approvalFile.getFilePathName());
                    return transformedFile;
                })
                .collect(Collectors.toList());

        /* expenseDetail 꺼내기 */
        List<ExpenseDetail> approvalExpenseDetail = approvalExpense.getExpenseDetail();

        List<Map<String,Object>> transformedExpenseDetail = approvalExpenseDetail.stream()
                .map(expenseDetail -> {
                    Map<String,Object> transformedExpenseDetails = new HashMap<>();
                    transformedExpenseDetails.put("expenseAccount", expenseDetail.getExpenseAccount());
                    transformedExpenseDetails.put("expenseDate", expenseDetail.getExpenseDate());
                    transformedExpenseDetails.put("expensePrice", expenseDetail.getExpensePrice());
                    transformedExpenseDetails.put("expenseBriefs", expenseDetail.getExpenseBriefs());
                    return transformedExpenseDetails;
                })
                .collect(Collectors.toList());


        return new ReportDetail_ExpenseResponse(
                approvalExpense.getApproval().getMember().getMemberInfo().getJob().getJobName(),
                approvalExpense.getApproval().getMember().getMemberInfo().getDept().getDeptName(),
                approvalExpense.getApproval().getMember().getMemberInfo().getInfoName(),
                transformedApprovalLines,
                transformedReferenceLines,
                approvalExpense.getApproval().getDocumentTitle(),
                approvalExpense.getApproval().getApprovalRegistDate(),
                approvalExpense.getApproval().getApprovalTurnbackReason(),
                transformedFiles,
                approvalExpense.getApproval().getDocumentType(),
                approvalExpense.getExpenseNote(),
                approvalExpense.getExpenseStatus(),
                transformedExpenseDetail
        );
    }
}

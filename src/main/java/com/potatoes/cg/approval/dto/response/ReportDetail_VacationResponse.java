package com.potatoes.cg.approval.dto.response;

import com.potatoes.cg.approval.domain.*;
import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;
import com.potatoes.cg.approval.domain.type.vacationType.VacationType;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ReportDetail_VacationResponse {

    /* 공통 정보 */
    private final String jobName;
    private final String deptName;
    private final String infoName;
    private final List<Map<String,Object>> approvalLine;
    private final List<Map<String,Object>> referenceLine;
    private final String documentTitle;
    private final LocalDateTime approvalRegistDate;
    private final String approvalTurnbackReason;
    private final DocumentType documentType;

    /*휴가 신청서 정보*/
    private final VacationType vacationType;
    private final LocalDate vacationStartDate;
    private final LocalDate vacationEndDate;
    private final String vacationBody;
    private final String vacationEmergencyPhone;

    /*로그인 유저 정보*/
    private final Long loginMember;

    public static ReportDetail_VacationResponse from(Vacation approvalVacation, Member loginUser) {
        /* 결재선에 필요한 정보만 가져오기 */
        List<ApprovalLine_GET> approvalLines = approvalVacation.getApproval().getApprovalLine_GET();

        List<Map<String, Object>> transformedApprovalLines = approvalLines.stream()
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
        List<ReferenceLine_GET> referenceLines = approvalVacation.getApproval().getReferenceLine_GET();

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

        return new ReportDetail_VacationResponse(
                approvalVacation.getApproval().getMember().getMemberInfo().getJob().getJobName(),
                approvalVacation.getApproval().getMember().getMemberInfo().getDept().getDeptName(),
                approvalVacation.getApproval().getMember().getMemberInfo().getInfoName(),
                transformedApprovalLines,
                transformedReferenceLines,
                approvalVacation.getApproval().getDocumentTitle(),
                approvalVacation.getApproval().getApprovalRegistDate(),
                approvalVacation.getApproval().getApprovalTurnbackReason(),
                approvalVacation.getApproval().getDocumentType(),
                approvalVacation.getVacationType(),
                approvalVacation.getVacationStartDate(),
                approvalVacation.getVacationEndDate(),
                approvalVacation.getVacationBody(),
                approvalVacation.getVacationEmergencyPhone(),
                loginUser.getMemberCode()
        );
    }
}

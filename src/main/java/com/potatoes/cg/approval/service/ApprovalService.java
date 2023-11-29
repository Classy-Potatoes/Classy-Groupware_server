package com.potatoes.cg.approval.service;

import com.potatoes.cg.approval.domain.Approval;
import com.potatoes.cg.approval.domain.ApprovalLine;
import com.potatoes.cg.approval.domain.Letter;
import com.potatoes.cg.approval.domain.Reference;
import com.potatoes.cg.approval.domain.repository.*;
import com.potatoes.cg.approval.dto.request.LetterCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final ApprovalLineRepository approvalLineRepository;
    private final LetterRepository letterRepository;
    private final FileEntityRepository fileEntityRepository;
    private final ReferenceRepository referenceRepository;


    @Value("${file.approval-url}")
    private String APPROVAL_URL;
    @Value("${file.approval-dir}")
    private String APPROVAL_DIR;


    private String getRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /* 기안서 작성 - 품의서 */
    @Transactional
    public Long letterSave(final LetterCreateRequest letterRequest/*, final MultipartFile attachment*/) {

        /* 전달 된 파일을 서버의 지정 경로에 저장 */
//        String replaceFileName = FileUploadUtils.saveFile(APPROVAL_DIR, getRandomName(), attachment);

        final List<Reference> referenceLine =
                letterRequest.getReferenceLine().stream().map(memberCode -> Reference.of(memberCode)).collect(Collectors.toList());

        final List<ApprovalLine> approvalLine =
                letterRequest.getApprovalLine().stream().map(turn -> ApprovalLine.of(turn)).collect(Collectors.toList());

        final Letter letterBody = Letter.of(letterRequest.getLetterBody());

        final Approval newLetter = Approval.of(
                letterRequest.getDocumentTitle(),
                referenceLine,
                approvalLine,
                letterRequest.getDocumentType()
        );


        final Approval approvalCode = approvalRepository.save(newLetter);
        final Letter LetterCode = letterRepository.save(letterBody);





        return approvalCode.getApprovalCode();
    }
}

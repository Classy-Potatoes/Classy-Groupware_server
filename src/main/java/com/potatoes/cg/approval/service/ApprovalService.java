package com.potatoes.cg.approval.service;

import com.potatoes.cg.approval.domain.*;
import com.potatoes.cg.approval.domain.repository.*;
import com.potatoes.cg.approval.dto.request.LetterCreateRequest;
import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.common.exception.type.ExceptionCode;
import com.potatoes.cg.common.util.MultipleFileUploadUtils;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final ApprovalLineRepository approvalLineRepository;
    private final LetterRepository letterRepository;
    private final FileEntityRepository fileEntityRepository;
    private final ReferenceRepository referenceRepository;
    private final MemberRepository memberRepository;



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
            if(attachment != null && !attachment.isEmpty()) {
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
                .orElseThrow( () -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER_ID));
        /* 참조자 memberCode  참조자는 안넣을수도 있기때문에 nullpointer 핸들링*/
        final List<Reference> referenceLine =
                Optional.ofNullable(letterRequest.getReferenceLine())
                                .orElse(Collections.emptyList())
                                        .stream()
                                                .map(memberCode -> Reference.of(memberCode))
                                                        .collect(Collectors.toList());
        /* 결재자 memberCode*/
        final List<ApprovalLine> approvalLine =
                letterRequest.getApprovalLine().stream().map(memberCode -> ApprovalLine.of(memberCode)).collect(Collectors.toList());


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
}

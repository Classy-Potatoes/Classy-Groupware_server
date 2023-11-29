package com.potatoes.cg.approval.presentation;

import com.potatoes.cg.approval.dto.request.LetterCreateRequest;
import com.potatoes.cg.approval.service.ApprovalService;
import com.potatoes.cg.common.util.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cg-api/v1/approval")
public class ApprovalController {

    private final ApprovalService approvalService;

    /* 기안서 작성 - 품의서  */
    @PostMapping("/letter")
    public ResponseEntity<Void> letterSave(@RequestBody @Valid final LetterCreateRequest letterRequest
                                           /*@RequestPart final MultipartFile attachment*/) {

        final Long letterCode = approvalService.letterSave(letterRequest);

        return ResponseEntity.created(URI.create("/letter/" + letterCode)).build();
    }
}

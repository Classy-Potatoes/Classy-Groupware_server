package com.potatoes.cg.approval.presentation;

import com.potatoes.cg.approval.dto.request.LetterCreateRequest;
import com.potatoes.cg.approval.service.ApprovalService;
import com.potatoes.cg.jwt.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cg-api/v1/approval")
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

    /* 기안서 작성 - 지출결의서 */
//    @PostMapping("/expense")
//    public ResponseEntity<Void> expenseSave(@RequestPart @Valid final expenseCreateRequest expenseRequest,
//                                            @AuthenticationPrincipal CustomUser customUser,
//                                            @RequestPart(required = false) final List<MultipartFile> attachment) {
//
//
//        return null;
//    }
}

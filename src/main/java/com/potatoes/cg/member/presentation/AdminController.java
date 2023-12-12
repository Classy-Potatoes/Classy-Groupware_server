package com.potatoes.cg.member.presentation;

import com.potatoes.cg.common.paging.Pagenation;
import com.potatoes.cg.common.paging.PagingButtonInfo;
import com.potatoes.cg.common.paging.PagingResponse;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.dto.request.InfoRegistRequest;
import com.potatoes.cg.member.dto.response.HistoryResponse;
import com.potatoes.cg.member.dto.response.NonMembersResponse;
import com.potatoes.cg.member.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/cg-api/v1/ad")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /* 회원 정보 가입(tbl_member_info) */
    @PostMapping("/member/info/regist")
    public ResponseEntity<Void> infoRegist(@RequestBody @Valid InfoRegistRequest infoRequest) {

        adminService.infoRegist( infoRequest );

        return ResponseEntity.status( HttpStatus.CREATED ).build();
    }

    /* 미분류 회원 목록 조회 */
    @GetMapping("/memberStatus")
    public ResponseEntity<PagingResponse> getNonMembers( @RequestParam(defaultValue = "1") final Integer page ) {

        final Page<NonMembersResponse> nonMembers = adminService.getNonMembers( page );
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo( nonMembers );
        final PagingResponse pagingResponse = PagingResponse.of( nonMembers.getContent(), pagingButtonInfo );

        return ResponseEntity.ok( pagingResponse );
    }



}

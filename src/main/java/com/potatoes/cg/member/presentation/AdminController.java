package com.potatoes.cg.member.presentation;

import com.potatoes.cg.common.paging.Pagenation;
import com.potatoes.cg.common.paging.PagingButtonInfo;
import com.potatoes.cg.common.paging.PagingResponse;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.dto.request.InfoRegistRequest;
import com.potatoes.cg.member.dto.request.MemberUpdateRequest;
import com.potatoes.cg.member.dto.response.*;
import com.potatoes.cg.member.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;

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


    /* 회원 목록 조회 */
    @GetMapping("/members")
    public ResponseEntity<PagingResponse> getAdminMembers(@RequestParam(defaultValue = "1") final Integer page ) {

        final Page<AdminMembersResponse> members = adminService.getAdminMembers( page );
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo( members );
        final PagingResponse pagingResponse = PagingResponse.of( members.getContent(), pagingButtonInfo );

        return ResponseEntity.ok( pagingResponse );
    }

    /* 회원 목록 조회(search) */
    @GetMapping("/members/search")
    public ResponseEntity<PagingResponse> getAdminMembersByInfoName(@RequestParam final String infoName,
                                                                    @RequestParam(defaultValue = "1") final Integer page ) {

        final Page<AdminMembersResponse> members = adminService.getAdminMembersByInfoName( page, infoName);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo( members );
        final PagingResponse pagingResponse = PagingResponse.of( members.getContent(), pagingButtonInfo );

        return ResponseEntity.ok( pagingResponse );
    }


    /* 회원 상세 조회(관리자) */
    @GetMapping("/members/{memberCode}")
    public ResponseEntity<ProfileResponse> getCustomAdminMember(@PathVariable final Long memberCode ) {

        final ProfileResponse profileResponse = adminService.getCustomAdminMember( memberCode );

        return ResponseEntity.ok( profileResponse );
    }


    /* 회원상세 수정(관리기능) */
    @PutMapping("/member/update/{memberCode}")
    public ResponseEntity<Void> adminProfileUpdate(@RequestPart @Valid final MemberUpdateRequest memberUpdateRequest,
                                              @RequestPart(required = false) final MultipartFile profileImage,
                                              @PathVariable final Long memberCode ) {

        adminService.adminProfileUpdate( memberUpdateRequest, profileImage, memberCode );

        return ResponseEntity.created( URI.create( "/" ) ).build();
    }


    /* 미등록 회원 목록 조회 */
    @GetMapping("/nonMembers")
    public ResponseEntity<PagingResponse> getNonMembers( @RequestParam(defaultValue = "1") final Integer page ) {

        final Page<NonMembersResponse> nonMembers = adminService.getNonMembers( page );
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo( nonMembers );
        final PagingResponse pagingResponse = PagingResponse.of( nonMembers.getContent(), pagingButtonInfo );

        return ResponseEntity.ok( pagingResponse );
    }


    /* 미등록 회원 목록 조회(search) */
    @GetMapping("/nonMembers/search")
    public ResponseEntity<PagingResponse> getNonMembersByInfoName(@RequestParam final String infoName,
                                                                  @RequestParam(defaultValue = "1") final Integer page ) {

        final Page<NonMembersResponse> nonMembers = adminService.getNonMembersByInfoName( page, infoName );
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo( nonMembers );
        final PagingResponse pagingResponse = PagingResponse.of( nonMembers.getContent(), pagingButtonInfo );

        return ResponseEntity.ok( pagingResponse );
    }

    /* 미등록 회원 삭제(관리자) */
    @DeleteMapping("/nonMembers/delete")
    public ResponseEntity<Void> deleteNonMember( @RequestParam final Long infoCode ) {

        adminService.delete( infoCode );

        return ResponseEntity.noContent().build();
    }

    /* 히스토리 삭제(관리자) */
    @DeleteMapping("/history/delete")
    public ResponseEntity<Void> deleteHistory( @RequestParam final Long historyCode ) {

        adminService.deleteHistory( historyCode );

        return ResponseEntity.noContent().build();
    }


}

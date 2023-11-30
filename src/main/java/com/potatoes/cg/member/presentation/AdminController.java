package com.potatoes.cg.member.presentation;

import com.potatoes.cg.member.dto.request.InfoRegistRequest;
import com.potatoes.cg.member.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

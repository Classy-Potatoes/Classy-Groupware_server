package com.potatoes.cg.member.presentation;

import com.potatoes.cg.member.dto.request.MemberSignupRequest;
import com.potatoes.cg.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping({"/member", "/cg-api/v1/member"})
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /* 1. 회원 가입 */
    @PostMapping("/regist")
    public ResponseEntity<Void> regist(@RequestBody @Valid MemberSignupRequest memberRequest) {

        memberService.regist( memberRequest );

        return ResponseEntity.status( HttpStatus.CREATED ).build();

    }

//    /* 프로필 조회 */
//    // @GetMapping 만 적었을 경우 상단에 /api/v1/member 그대로 적용됨
//    @GetMapping
//    public ResponseEntity<ProfileResponse> profile(@AuthenticationPrincipal User user ) {
//
//        ProfileResponse profileResponse = memberService.getProfile( user.getUsername() );
//
//        return ResponseEntity.ok( profileResponse );
//    }


}

package com.potatoes.cg.member.presentation;

import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.dto.request.MemberSearchIdRequest;
import com.potatoes.cg.member.dto.request.MemberSignupRequest;
import com.potatoes.cg.member.dto.response.MemberResponse;
import com.potatoes.cg.member.dto.response.ProfileResponse;
import com.potatoes.cg.member.dto.response.SearchInfoResponse;
import com.potatoes.cg.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping({"/cg-api/v1", "/cg-api/v1/member", "/cg-api/v1/non"})
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    /* 사번 검증 */
    @GetMapping("/member/info/search")
    public ResponseEntity<SearchInfoResponse> infoSearch(@RequestParam final Long infoCode) {

        SearchInfoResponse searchInfoResponse = memberService.infoSearch( infoCode );

        return ResponseEntity.ok( searchInfoResponse );
    }


    /* 계정 가입 */
    @PostMapping("/member/regist")
    public ResponseEntity<Void> regist(@RequestBody @Valid final MemberSignupRequest memberRequest) {

        memberService.regist( memberRequest );

        return ResponseEntity.status( HttpStatus.CREATED ).build();

    }


    /* 아이디 찾기 */
    @GetMapping("/member/search")
    public ResponseEntity<MemberResponse> searchId(@RequestBody @Valid final MemberSearchIdRequest memberSearchIdRequest) {

        MemberResponse memberResponse = memberService.searchId( memberSearchIdRequest );

        return ResponseEntity.ok( memberResponse );
    }


    /* 비밀번호 찾기 */
    @GetMapping("/member/pwdSearch")
    public ResponseEntity<MemberResponse> pwdSearch(@RequestBody @Valid final MemberSearchIdRequest memberSearchIdRequest) {

        MemberResponse memberResponse = memberService.searchId( memberSearchIdRequest );

        return ResponseEntity.ok( memberResponse );
    }


    /* 아이디 중복 검사 */
    @GetMapping("/member/duplicateId")
    public ResponseEntity<String> duplicateId(@RequestParam final String inputMemberId) {

        String memberId = memberService.duplicateId( inputMemberId );

        return ResponseEntity.ok( memberId );
    }


//    @GetMapping()
//    public ResponseEntity<ProfileResponse> profile(@AuthenticationPrincipal User user ) {
//
//        ProfileResponse profileResponse = memberService.getProfile( user.getUsername() );
//
//        return ResponseEntity.ok( profileResponse );
//    }


}

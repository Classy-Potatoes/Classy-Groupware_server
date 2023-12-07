package com.potatoes.cg.member.dto.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;

@Getter
//@RequiredArgsConstructor
public class InfoCodeCheckRequest {

    // json 으로 넘길때 파라미터가 1개일때 @RequiredArgsConstructor 사용 못하는 이슈
    // 그냥 final 제거후 사용

    private Long infoCode;

}

/* args 한개만 받을때는 기본생성자가 필요함
    @NoArgsConstructor를 붙여도 되고 아니면 @JsonPropoerty 붙여도 해결됨
    근데 사실 생성자를 안만들면 알아서 만들어주니까...
    DTO에는 굳이 @NoArgsConstructor를 안붙여도 된다는 결론...!
*/
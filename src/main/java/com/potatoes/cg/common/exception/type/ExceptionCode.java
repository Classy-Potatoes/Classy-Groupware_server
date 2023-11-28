package com.potatoes.cg.common.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

//    FAIL_TO_UPLOAD_FILE(1001, "파일 저장에 실패하였습니다."),
//    FAIL_TO_DELETE_FILE(1002, "파일 삭제에 실패하였습니다." ),
//    NOT_FOUND_CATEGORY_CODE(2000, "카테고리 코드에 해당하는 카테고리가 존재하지 않습니다."),
//    NOT_FOUND_PRODUCT_CODE(3000, "상품 코드에 해당하는 상품이 존재하지 않습니다."),

    /* 로그인 실패에 대한 코드 */
    FAIL_LOGIN(1400, "로그인에 실패하였습니다."),
    UNAUTHORIZED(1401, "인증 되지 않은 요청입니다."),
    NOT_FOUND_MEMBER_ID(1402, "아이디에 해당하는 유저가 없습니다."),
    ACCESS_DENIED(1403, "허가 되지 않은 요청입니다."),


    /* 멤버 조회 실패 코드 */


//    NOT_FOUND_MEMBER_CODE(4004, "멤버 코드에 해당하는 유저가 없습니다."),
//    NOT_ENOUGH_STOCK(5000, "재고 부족으로 주문 불가합니다."),
//    NOT_FOUND_VALID_ORDER(5001, "유효한 주문 건이 없습니다."),
//    NOT_FOUND_REVIEW_CODE(6000, "리뷰 코드에 해당하는 리뷰가 존재하지 않습니다."),
    ALREADY_EXIST_REVIEW(6001, "이미 리뷰가 작성되어 작성할 수 없습니다."),

    NOT_FOUND_NOTE_CODE(3000, "쪽지 코드에 해당하는 쪽지가 없습니다.");


    /* enum 쪽에서 필드를 생성이 가능하고, 기본 생성자와 getter를 생성했기 때문에
    * 관련된것을 호출할수 있도록 만든다. */
    private final int code;
    private final String message;

}

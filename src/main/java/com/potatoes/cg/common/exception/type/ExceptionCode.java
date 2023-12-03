package com.potatoes.cg.common.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    /* common */
    FAIL_TO_UPLOAD_FILE(9001, "파일 저장에 실패하였습니다."),
    FAIL_TO_DELETE_FILE(9002, "파일 삭제에 실패하였습니다."),

    /* HS */
    FAIL_LOGIN(1400, "로그인에 실패하였습니다."),
    UNAUTHORIZED(1401, "인증 되지 않은 요청입니다."),
    ACCESS_DENIED(1403, "허가 되지 않은 요청입니다."),
    NOT_FOUND_MEMBER_ID(1421, "아이디에 해당하는 유저가 없습니다."),
    NOT_FOUND_JOB_CODE(1422, "직급 코드에 해당하는 직급이 존재하지 않습니다."),
    NOT_FOUND_DEPT_CODE(1423, "부서 코드에 해당하는 부서가 존재하지 않습니다."),
    NOT_FOUND_INFO_CODE(1424, "존재하지 않는 사번입니다."),
    NOT_FOUND_INFO_CODE_AND_INFO_NAME(1425, "해당하는 사번과 이름에 일치하는 아이디를 찾지 못했습니다."),

    /* JH(준) */


    /* IS */
    NOT_PROJECT_CODE(3000, "프로젝트코드에 해당하는 프로젝트가 존재하지 않습니다."),
    NOT_FOUND_POST_CODE(3001, "존재하지 않은 게시글입니다."),

    /* JH(주) */
    NOT_FOUND_NOTE_CODE(5000, "쪽지 코드에 해당하는 쪽지가 없습니다."),
    NOT_FOUND_NOTE(5001, "검색한 키워드에 일치하는 쪽지가 없습니다."),

    /* WS */




    ALREADY_EXIST_REVIEW(6001, "이미 리뷰가 작성되어 작성할 수 없습니다."),
    NOT_FOUND_VALID_TITLE(4005, "제목을 입력해주세요."),
    NOT_FOUND_VALID_DATE(4006, "날짜를 입력해주세요."),
    NOT_FOUND_CALENDAR_CODE(4007, "등록된 일정이 없습니다.");



    /* enum 쪽에서 필드를 생성이 가능하고, 기본 생성자와 getter를 생성했기 때문에
     * 관련된것을 호출할수 있도록 만든다. */
    private final int code;
    private final String message;

}
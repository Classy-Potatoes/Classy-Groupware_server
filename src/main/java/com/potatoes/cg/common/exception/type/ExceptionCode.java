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
    NOT_FOUND_MEMBER_CODE(1422, "계정코드가 존재하지 않습니다."),
    NOT_FOUND_MEMBER_NON_STATUS(1423, "계정을 사용할 수 없습니다."),
    NOT_FOUND_JOB_CODE(1424, "직급 코드에 해당하는 직급이 존재하지 않습니다."),
    NOT_FOUND_DEPT_CODE(1425, "부서 코드에 해당하는 부서가 존재하지 않습니다."),
    NOT_FOUND_INFO_CODE(1426, "존재하지 않는 사번입니다."),
    NOT_FOUND_INFO_CODE_AND_INFO_NAME(1427, "해당하는 사번과 이름에 일치하는 아이디를 찾지 못했습니다."),
    NOT_FOUND_INFO_CODE_AND_MEMBER_ID(1428, "해당하는 사번과 아이디가 일치하지 않습니다."),

    /* JH(준) */
    NOT_FOUND_APPROVAL_CODE(7001, "해당하는 문서가 없습니다."),
    NOT_FOUND_APPROVALLINE_CODE (7002, "해당하는 결재선이 없습니다."),

    /* IS */
    NOT_PROJECT_CODE(3000, "프로젝트코드에 해당하는 프로젝트가 존재하지 않습니다."),
    NOT_FOUND_POST_CODE(3001, "존재하지 않은 게시글입니다."),
    NOT_FOUND_REPLY_CODE(3002, "존재하지 않은 댓글입니다. "),

    /* JH(주) */
    NOT_FOUND_NOTE_CODE(5000, "쪽지 코드에 해당하는 쪽지가 없습니다."),
    NOT_FOUND_NOTE(5001, "검색한 키워드에 일치하는 쪽지가 없습니다."),

    /* WS */
    ALREADY_EXIST_REVIEW(6001, "이미 리뷰가 작성되어 작성할 수 없습니다."),
    NOT_FOUND_VALID_TITLE(4005, "제목을 입력해주세요."),
    NOT_FOUND_VALID_DATE(4006, "날짜를 입력해주세요."),
    NOT_FOUND_CALENDAR_CODE(4007, "등록된 일정이 없습니다."),
    NOT_VALID_DATE(4008, "시작일이 종료일보다 이전이어야 합니다."),
    NOT_FOUND_MEMBERCODE(4009, "해당하는 멤버가 없습니다."),
    NOT_FOUND_SCHEDULE_CODE(4010, "등록된 프로젝트 일정이 없습니다."),
    NOT_FOUND_TODO_CODE(4011, "등록된 프로젝트 할일이 없습니다."),
    NOT_FOUND_PROJECT_CODE(4011, "등록된 프로젝트번호가 없습니다."),
    NOT_FOUND_MEMBER(4012, "참석자가 한명은 등록되어야합니다."),
    NOT_FOUND_REPLY_BODY(4013, "작성된 댓글 내용이 없습니다.");




    /* enum 쪽에서 필드를 생성이 가능하고, 기본 생성자와 getter를 생성했기 때문에
     * 관련된것을 호출할수 있도록 만든다. */
    private final int code;
    private final String message;

}
package com.potatoes.cg.notice.dto.request;

import com.potatoes.cg.notice.domain.type.NoticeOptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class NoticeCreateRequest {

    @Min(value = 1)
    private final Long noticeCode;

    @NotBlank
    private final NoticeOptionType noticeOption; //공지 게시판 타입(일반, 필독)

    @NotBlank
    private final String noticeTitle; //공지 게시판 제목

    @NotBlank
    private final String noticeBody; //공지 게시판 내용

}
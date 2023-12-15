package com.potatoes.cg.notice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class NoticesResponse {

    private final String NoticeOptionType;

    private final String NoticeTitle;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime noticeRegisterDate;

}
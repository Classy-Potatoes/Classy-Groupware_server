package com.potatoes.cg.notice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class NoticeResponse {

    private final String NoticeOptionType;

    private final Long deptCode;

    private final String deptName;

    private final Long memberCode;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDateTime noticeRegisterDate;

    private final String NoticeTitle;

    private final String NoticeBody;

}
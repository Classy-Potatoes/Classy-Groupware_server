package com.potatoes.cg.member.dto.response;

import com.potatoes.cg.member.domain.History;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class HistoryResponse {

    private final Long historyCode;
    private final String historyDate;
    private final String historyCategory;
    private final String historyJobCode;
    private final String historyDeptCode;
    private final String historyNote;


    public static HistoryResponse from( final History history ) {

        return new HistoryResponse(
                history.getHistoryCode(),
                history.getHistoryDate(),
                history.getHistoryCategory(),
                history.getJob().getJobName(),
                history.getDept().getDeptName(),
                history.getHistoryNote()
        );

    }

}

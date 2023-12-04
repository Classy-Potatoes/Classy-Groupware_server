package com.potatoes.cg.member.dto.response;

import com.potatoes.cg.member.domain.History;
import com.potatoes.cg.member.domain.Job;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class JobResponse {

    private final Long jobCode;
    private final String jobName;

    public static JobResponse from( final Job job ) {

        return new JobResponse(
                job.getJobCode(),
                job.getJobName()
        );

    }

}

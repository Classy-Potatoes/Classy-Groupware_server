package com.potatoes.cg.project.dto.request;

import com.potatoes.cg.member.domain.Dept;
import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@RequiredArgsConstructor
@Getter
public class ProjectCreateRequest {

    @NotBlank
    private final String projectTitle;

    @NotBlank
    private final String projectBody;

    private final Date projectStartDate;

    private final Date projectEndDate;

    @Min(value = 1)
    private final Long deptCode;

    @Min(value = 1)
    private final Long memberCode;

}

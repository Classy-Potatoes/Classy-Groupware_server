package com.potatoes.cg.project.dto.request;

import com.potatoes.cg.project.domain.type.ProjectStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@RequiredArgsConstructor
@Getter
public class ProjectUpdateRequest {

    @NotBlank
    private final String projectTitle;

    @NotBlank
    private final String projectBody;

    private final Date projectStartDate;

    private final Date projectEndDate;

    @Min(value = 1)
    private final Long deptCode;

//    @Min(value = 1)
//    private final Long memberCode;
//
//    private final ProjectStatusType status;
}

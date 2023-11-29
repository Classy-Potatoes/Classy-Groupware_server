package com.potatoes.cg.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.type.ProjectStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@RequiredArgsConstructor
public class ProjectResponse {

    private final Long projectCode;
    private final String projectTitle;
    private final String projectBody;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final Date projectStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final Date projectEndDate;
    private final ProjectStatusType projectStatus;
    private final Long deptCode;
    private final String deptName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime projectCreatedDate;
    private final int memberCode;

}

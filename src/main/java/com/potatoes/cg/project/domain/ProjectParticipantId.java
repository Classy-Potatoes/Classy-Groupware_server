package com.potatoes.cg.project.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProjectParticipantId implements Serializable {

    @Column(name = "project_code")
    private Long projectCode;

    @Column(name = "info_code")
    private Long memberCode;

    public ProjectParticipantId(Long projectCode, Long memberCode) {
        this.projectCode = projectCode;
        this.memberCode = memberCode;
    }

    public ProjectParticipantId() {
        // 기본 생성자
    }

// equals, hashCode 등 필요한 메서드 추가
}
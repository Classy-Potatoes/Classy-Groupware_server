package com.potatoes.cg.project.domain;

import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_project_participant")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ProjectParticipant implements Serializable {

    @EmbeddedId
    private ProjectParticipantId id;

    @ManyToOne
    @JoinColumn(name = "project_code", insertable = false, updatable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "member_code", insertable = false, updatable = false)
    private Member member;

    public ProjectParticipant(Project project, Member member) {
        this.id = new ProjectParticipantId(project.getProjectCode(), member.getMemberCode());
        this.project = project;
        this.member = member;
    }
}
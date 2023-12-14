package com.potatoes.cg.projectManagers.domain;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.project.domain.type.ProjectOptionType;
import com.potatoes.cg.projectTodolist.domain.ProjectTodolist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import static com.potatoes.cg.project.domain.type.ProjectOptionType.SCHEDULE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_project_manager")
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
public class ProjectManagersSchedule {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long projectManagerCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberCode")
    private Member member;

    @Column(name = "scheduleCode")
    private Long scheduleCode;

    @Column(name = "projectOption")
    @Enumerated(value = STRING)
    private ProjectOptionType projectOptionType = SCHEDULE;

    public ProjectManagersSchedule(Member member) {
        this.member = member;
    }

    public static ProjectManagersSchedule of(Member member) {
        return new ProjectManagersSchedule(
                member
        );
    }

    public ProjectManagersSchedule(Long scheduleCode) {

        this.scheduleCode = scheduleCode;
    }

}

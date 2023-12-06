package com.potatoes.cg.projectManagers.domain;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.projectTodolist.domain.ProjectTodolist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tbl_project_manager")
//@NoArgsConstructor(access = PROTECTED)
@NoArgsConstructor
@Setter
@Getter
public class ProjectManagers {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long projectManagerCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberCode")
    private Member member;

    private Long scheduleCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "todoListCode", nullable = false)
    private ProjectTodolist todoList;

    public ProjectManagers(Member member) {
        this.member = member;
    }

    public static ProjectManagers of(Member member) {
        return new ProjectManagers(
                member
        );
    }

    public ProjectManagers(Long scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

}

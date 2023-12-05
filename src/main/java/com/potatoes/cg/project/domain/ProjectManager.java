package com.potatoes.cg.project.domain;

import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.project.domain.type.ProjectFileType;
import com.potatoes.cg.project.domain.type.ProjectOptionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "tbl_project_manager")
@NoArgsConstructor
@Getter
public class ProjectManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectManagerCode;

//    @ManyToOne
//    @JoinColumn(name = "memberCode")
//    private MemberInfo member;

    @Column
    private Long memberCode;

//    private String infoName;

    @Column(name = "projectOption")
    @Enumerated(value = STRING)
    private ProjectOptionType projectOptionType;

    public ProjectManager(Long infoCode) {
        this.memberCode = infoCode;
        this.projectOptionType = ProjectOptionType.TASK;
    }
}

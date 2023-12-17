package com.potatoes.cg.projectManagers.domain;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.project.domain.type.ProjectOptionType;
import com.potatoes.cg.projectTodolist.domain.ProjectTodolist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.List;

import static com.potatoes.cg.project.domain.type.ProjectOptionType.TODO;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_project_manager")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ProjectManagersTodo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long projectManagerCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberCode")
    private Member member;

    @Column(name = "projectOption")
    @Enumerated(value = STRING)
    private ProjectOptionType projectOptionType = TODO;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "todoListCode", referencedColumnName = "todoListCode")
    private ProjectTodolist todoList;

    public ProjectManagersTodo(Member member) {
        this.member = member;
    }

    public static ProjectManagersTodo of(Member member) {
        return new ProjectManagersTodo(
                member
        );
    }

}

package com.potatoes.cg.projectTodo.domain;

import com.potatoes.cg.calendar.domain.type.StatusType;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.ProjectReply;
import com.potatoes.cg.projectTodolist.domain.ProjectTodolist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static com.potatoes.cg.calendar.domain.type.StatusType.PROGRESS;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_project_todo")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tbl_project_todo SET todo_status = 'DELETED' WHERE todo_code = ?")
public class ProjectTodo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long todoCode;

    @Column(nullable = false)
    private String todoTitle;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime todoCreatedDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime todoModifyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberCode")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectCode")
    private Project project;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private StatusType todoStatus = PROGRESS;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "todoCode")
    private List<ProjectTodolist> projectTodolist;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "todoListCode")
    private List<ProjectReply> replies;

    public ProjectTodo(String todoTitle, Member member, Project project, List<ProjectTodolist> projectTodolist) {
        this.todoTitle = todoTitle;
        this.member = member;
        this.project = project;
        this.projectTodolist = projectTodolist;
    }

    public static ProjectTodo of(String todoTitle, Member member, Project project, List<ProjectTodolist> projectTodolists) {
        return new ProjectTodo(
                todoTitle,
                member,
                project,
                projectTodolists
        );
    }


    public void update(String todoTitle, Project project, List<ProjectTodolist> projectTodolists) {
        this.todoTitle = todoTitle;
        this.project = project;
        this.projectTodolist = projectTodolists;

    }
}


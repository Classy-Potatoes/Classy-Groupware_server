package com.potatoes.cg.projectTodolist.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.calendar.domain.type.StatusType;
import com.potatoes.cg.projectManagers.domain.ProjectManagers;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

import static com.potatoes.cg.calendar.domain.type.StatusType.UNFINISHED;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_project_todolist")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ProjectTodolist {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long todoListCode;

    @Column
    private Long todoCode;

    @Column(nullable = false)
    private String todoBody;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate todoEndDate;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private StatusType tofoStatus = UNFINISHED;

    @OneToOne(mappedBy = "todoList", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProjectManagers projectManager;

    public ProjectTodolist(String todoBody, LocalDate todoEndDate, ProjectManagers projectManager) {
        this.todoBody = todoBody;
        this.todoEndDate = todoEndDate;
        this.projectManager = projectManager;
    }

    public static ProjectTodolist of(String todoBody, LocalDate endDates, ProjectManagers projectManager) {
        return new ProjectTodolist(
                todoBody,
                endDates,
                projectManager
        );
    }

    public void update(String todoBody, LocalDate endDates, ProjectManagers projectManager) {
        this.todoBody = todoBody;
        this.todoEndDate = endDates;
        this.projectManager = projectManager;
    }
}


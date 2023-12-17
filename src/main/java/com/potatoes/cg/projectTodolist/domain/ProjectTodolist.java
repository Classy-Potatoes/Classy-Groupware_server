package com.potatoes.cg.projectTodolist.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.calendar.domain.type.StatusType;
import com.potatoes.cg.project.domain.ProjectReply;
import com.potatoes.cg.projectManagers.domain.ProjectManagersTodo;
import com.potatoes.cg.projectTodolist.dto.request.ProjectTodolistUpdateRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static com.potatoes.cg.calendar.domain.type.StatusType.UNFINISHED;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_project_todolist")
@NoArgsConstructor
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

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "todoList")
    @PrimaryKeyJoinColumn
    private ProjectManagersTodo projectManager;

    public ProjectTodolist(String todoBody, LocalDate todoEndDate, ProjectManagersTodo projectManagersTodo) {
        this.todoBody = todoBody;
        this.todoEndDate = todoEndDate;
        this.projectManager = projectManagersTodo;
    }

    public ProjectTodolist(Long a, String todoBody, LocalDate todoEndDate, ProjectManagersTodo projectManagersTodo) {
        this.todoListCode = a;
        this.todoBody = todoBody;
        this.todoEndDate = todoEndDate;
        this.projectManager = projectManagersTodo;
    }

    public static ProjectTodolist of(String todoBody, LocalDate endDates, ProjectManagersTodo projectManagersTodo) {
        return new ProjectTodolist(
                todoBody,
                endDates,
                projectManagersTodo
        );
    }

    public static ProjectTodolist ofs(Long a, String todoBody, LocalDate endDates, ProjectManagersTodo projectManagersTodo) {
        return new ProjectTodolist(
                a,
                todoBody,
                endDates,
                projectManagersTodo
        );
    }

    public void update(List<ProjectTodolistUpdateRequest> todoRequest) {

    }

    public void checked(StatusType newStatus) {
        this.tofoStatus = newStatus;
    }
}


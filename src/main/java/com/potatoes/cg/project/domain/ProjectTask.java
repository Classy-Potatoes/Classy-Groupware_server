package com.potatoes.cg.project.domain;

import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.project.domain.type.ProjectStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.potatoes.cg.project.domain.type.ProjectStatusType.USABLE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_project_task")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tbl_project_task SET task_delete_status = 'DELETED' WHERE task_code = ?")
public class ProjectTask {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private  Long taskCode;

    @Column(nullable = false)
    private String taskTitle;

    @Column(nullable = false)
    private String taskBody;

    @Column
    private String taskStatus = "요청";

    @Column(nullable = false)
    private Date taskStartDate;

    @Column(nullable = false)
    private Date taskEndDate;

    @Column(nullable = false)
    private String taskPriority;

    @ManyToOne
    @JoinColumn(name = "memberCode", referencedColumnName = "infoCode")
    private MemberInfo member;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime taskRequestDate;

    @Column(nullable = false)
    private LocalDateTime taskProgressDate;

    @Column(nullable = false)
    private LocalDateTime taskCompleteDate;

    @Column(nullable = false)
    private LocalDateTime taskDeferDate;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private ProjectStatusType taskDeleteStatus = USABLE;

    @Column
    private Long projectCode;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "taskCode")
    private List<ProjectFile> fileEntity;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "taskCode")
    private List<ProjectManager> projectManagers;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "taskCode")
    private List<ProjectReply> replies;


    public ProjectTask(String taskTitle, String taskBody, Date taskStartDate, Date taskEndDate,
                       String taskPriority, MemberInfo member,  Long projectCode, List<ProjectFile> fileEntity,
                       List<ProjectManager> projectManagers) {
        this.taskTitle = taskTitle;
        this.taskBody = taskBody;
        this.taskStartDate = taskStartDate;
        this.taskEndDate = taskEndDate;
        this.taskPriority = taskPriority;
        this.member = member;
        this.projectCode = projectCode;
        this.fileEntity = fileEntity;
        this.projectManagers = projectManagers;
    }


    /* 생성 */
    public static ProjectTask of(Long projectCode, MemberInfo member, String taskTitle,
                                 String taskBody, Date taskStartDate, Date taskEndDate,
                                 String taskPriority, List<ProjectFile> fileEntity, List<ProjectManager> managers) {

        return new ProjectTask(
                taskTitle,
                taskBody,
                taskStartDate,
                taskEndDate,
                taskPriority,
                member,
                projectCode,
                fileEntity,
                managers
        );
    }

    /* 수정 */
    public void update(String taskTitle, String taskBody, Date taskStartDate,
                       Date taskEndDate, String taskPriority) {
        this.taskTitle = taskTitle;
        this.taskBody = taskBody;
        this.taskStartDate = taskStartDate;
        this.taskEndDate = taskEndDate;
        this.taskPriority = taskPriority;
    }

    public void updateCheck(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}

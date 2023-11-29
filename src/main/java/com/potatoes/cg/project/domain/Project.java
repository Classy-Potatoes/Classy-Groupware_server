package com.potatoes.cg.project.domain;

import com.potatoes.cg.member.domain.Dept;
import com.potatoes.cg.member.domain.Member;
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
@Table(name = "tbl_project")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tbl_project SET status = 'DELETED' WHERE project_code = ?")
public class Project {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long projectCode;

    @Column(nullable = false)
    private String projectTitle;

    @Column(nullable = false)
    private String projectBody;

    @Column(nullable = false)
    private Date projectStartDate;

    @Column(nullable = false)
    private Date projectEndDate;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private ProjectStatusType projectStatus = USABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deptCode")
    private Dept dept;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime projectCreatedDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime projectModifyDate;

//    @ManyToOne
//    @JoinColumn(name = "memberCode")
//    private Member member;

    @Column
    private int memberCode;

    @OneToMany(mappedBy = "project")
    private List<ProjectParticipant> participants;

    public Project(String projectTitle,String projectBody,Date projectStartDate,
                   Date projectEndDate,Dept dept, int memberCode){
        this.projectTitle =projectTitle;
        this.projectBody = projectBody;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.dept = dept;
        this.memberCode = memberCode;
    }
    public static Project of(
            final String projectTitle, final String projectBody,
            final Date projectStartDate, final Date projectEndDate, final Dept dept, final int memberCode
    ) {

        return new Project(
                projectTitle,
                projectBody,
                projectStartDate,
                projectEndDate,
                dept,
                memberCode
        );
    }
}

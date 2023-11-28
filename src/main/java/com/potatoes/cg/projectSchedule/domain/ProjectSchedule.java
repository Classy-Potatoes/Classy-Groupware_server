package com.potatoes.cg.projectSchedule.domain;

import com.potatoes.cg.calendar.domain.type.StatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.potatoes.cg.calendar.domain.type.StatusType.PROGRESS;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_project_schedule")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tbl_project_scheddule SET status = 'DELETED' WHERE schedule_code = ?")
public class ProjectSchedule {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long scheduleCode;

    @Column(nullable = false)
    private String scheduleTitle;

    @Column(nullable = false)
    private String scheduleBody;

    @Column(nullable = false)
    private LocalDateTime scheduleStartDate;

    @Column(nullable = false)
    private LocalDateTime scheduleEndDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime scheduleCreatedDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime scheduleModifyDate;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private StatusType scheduleStatus = PROGRESS;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "memberCode")
//    private Member member;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "projectCode")
//    private Project project;
}

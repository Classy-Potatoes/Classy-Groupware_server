package com.potatoes.cg.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_history")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class History {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long historyCode;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime historyDate;

    @Column(nullable = false)
    private String historyCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deptCode")
    private Dept dept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobCode")
    private Job job;

    @Column(nullable = false)
    private String historyNote;

    private Long infoCode;

    public History(String historyCategory, Dept dept, Job job, String historyNote) {
        this.historyCategory = historyCategory;
        this.dept = dept;
        this.job = job;
        this.historyNote = historyNote;
    }

    public static History of(String historyCategory, Dept dept, Job job, String historyNote) {

        return new History(
                historyCategory,
                dept,
                job,
                historyNote
        );

    }

}

package com.potatoes.cg.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_job")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Job {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long jobCode;

    @Column(nullable = false)
    private String jobName;

    public Job(Long jobCode) {
        this.jobCode = jobCode;
    }

    public static Job of(Long jobCode) {
        return new Job(jobCode);
    }
}

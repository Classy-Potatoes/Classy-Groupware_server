package com.potatoes.cg.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_dept")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Dept {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long deptCode;

    @Column(nullable = false)
    private String deptName;

    private String deptDescribe;

}
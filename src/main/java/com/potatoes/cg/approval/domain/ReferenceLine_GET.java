package com.potatoes.cg.approval.domain;

import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tbl_reference")
@Getter
@NoArgsConstructor
public class ReferenceLine_GET {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long referenceCode;

    private Long approvalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberCode")
    private Member member;



}

package com.potatoes.cg.approval.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tbl_reference")
@Getter
@NoArgsConstructor
public class Reference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long referenceCode;

    private Long approvalCode;

    private Long memberCode;

//    @ManyToOne
//    @JoinColumn(name = "memberCode")
//    private Member member;


//    public Reference(Member member) {
//        this.member = member;
//    }

    public Reference(Long memberCode) {
        this.memberCode = memberCode;
    }
    public static Reference of(Long memberCode) {

        return new Reference(memberCode);

    }
}

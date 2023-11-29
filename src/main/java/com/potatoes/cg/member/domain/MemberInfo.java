package com.potatoes.cg.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name="tbl_member_info")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class MemberInfo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long infoCode;

    @Column(nullable = false)
    private String infoName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobCode")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deptCode")
    private Dept dept;

    @Column(nullable = false)
    private String infoEmail;

    private String infoPhone;

    private Long infoZipcode;

    private String infoAddress;

    private String infoAddressAdd;



    public MemberInfo( String infoName, Job job, Dept dept, String infoEmail, String infoPhone,
                      Long infoZipcode, String infoAddress, String infoAddressAdd ) {
        this.infoName = infoName;
        this.job = job;
        this.dept = dept;
        this.infoEmail = infoEmail;
        this.infoPhone = infoPhone;
        this.infoZipcode = infoZipcode;
        this.infoAddress = infoAddress;
        this.infoAddressAdd = infoAddressAdd;
    }

    public static MemberInfo of( String infoName, Job job, Dept dept, String infoEmail, String infoPhone,
                                Long infoZipcode, String infoAddress, String infoAddressAdd ) {

        return new MemberInfo(
                infoName,
                job,
                dept,
                infoEmail,
                infoPhone,
                infoZipcode,
                infoAddress,
                infoAddressAdd
        );

    }


}

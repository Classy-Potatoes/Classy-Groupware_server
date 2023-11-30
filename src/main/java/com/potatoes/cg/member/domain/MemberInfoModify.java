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
public class MemberInfoModify {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long infoCode;

    @Column(nullable = false)
    private String infoName;

    @Column(nullable = false)
    private Long jobCode;

    @Column(nullable = false)
    private Long deptCode;

    @Column(nullable = false)
    private String infoEmail;

    private String infoPhone;

    private Long infoZipcode;

    private String infoAddress;

    private String infoAddressAdd;



    public MemberInfoModify(String infoName, Long jobCode, Long deptCode, String infoEmail, String infoPhone,
                            Long infoZipcode, String infoAddress, String infoAddressAdd ) {
        this.infoName = infoName;
        this.jobCode = jobCode;
        this.deptCode = deptCode;
        this.infoEmail = infoEmail;
        this.infoPhone = infoPhone;
        this.infoZipcode = infoZipcode;
        this.infoAddress = infoAddress;
        this.infoAddressAdd = infoAddressAdd;
    }

    public static MemberInfoModify of(String infoName, Long jobCode, Long deptCode, String infoEmail, String infoPhone,
                                      Long infoZipcode, String infoAddress, String infoAddressAdd ) {

        return new MemberInfoModify(
                infoName,
                jobCode,
                deptCode,
                infoEmail,
                infoPhone,
                infoZipcode,
                infoAddress,
                infoAddressAdd
        );

    }


    public void update(String infoName, Long deptCode, Long jobCode, String infoEmail, String infoPhone,
                       Long infoZipcode, String infoAddress, String infoAddressAdd) {

        this.infoName = infoName;
        this.deptCode = deptCode;
        this.jobCode = jobCode;
        this.infoEmail = infoEmail;
        this.infoPhone = infoPhone;
        this.infoZipcode = infoZipcode;
        this.infoAddress = infoAddress;
        this.infoAddressAdd = infoAddressAdd;
    }
}

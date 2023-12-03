package com.potatoes.cg.member.domain;

import com.potatoes.cg.member.domain.type.MemberRole;
import com.potatoes.cg.member.domain.type.MemberStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static com.potatoes.cg.member.domain.type.MemberRole.USER;
import static com.potatoes.cg.member.domain.type.MemberStatus.ACTIVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name="tbl_member")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long memberCode;

    @Column(nullable = false)
    private String memberId;

    @Column(nullable = false)
    private String memberPassword;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private MemberStatus memberStatus = ACTIVE;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private MemberRole memberRole = USER;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime memberJoinDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime memberUpdateDate;

    private String memberToken;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "infoCode")
    private MemberInfo memberInfo;


    public Member(String memberId, String memberPassword, MemberInfo memberInfo) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberInfo = memberInfo;
    }

    public static Member of(String memberId, String memberPassword, MemberInfo memberInfo) {

        return new Member(
                memberId,
                memberPassword,
                memberInfo
        );

    }

    public void updateRefreshToken( String memberToken ) {
        this.memberToken = memberToken;
    }

    // 이메일 임시비밀번호 변경
    public void updatePwd( String memberPassword ) {
        this.memberPassword = memberPassword;
    }


}

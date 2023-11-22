package com.potatoes.cg.member.domain;

import com.ohgiraffers.comprehensive.member.domain.type.MemberRole;
import com.ohgiraffers.comprehensive.member.domain.type.MemberStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.ohgiraffers.comprehensive.member.domain.type.MemberRole.USER;
import static com.ohgiraffers.comprehensive.member.domain.type.MemberStatus.ACTIVE;
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
    private String memberName;

    private String memberEmail;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private MemberRole memberRole = USER;

    // 상단에 @EntityListeners(AuditingEntityListener.class) 작성해서 계속 감지해준다.
    // JPA에서 @CreatedDate, @LastModifiedDate 선언시 자동으로 기록해준다.
    // updatable = false 한번 기록된것은 수정되지 못하게 하는 방식
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private MemberStatus status = ACTIVE;

    private String refreshToken;


    public Member(String memberId, String memberPassword, String memberName, String memberEmail) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
    }


    public static Member of(String memberId, String memberPassword, String memberName, String memberEmail) {

        return new Member(
                memberId,
                memberPassword,
                memberName,
                memberEmail
        );

    }

    public void updateRefreshToken( String refreshToken ) {
        this.refreshToken = refreshToken;
    }


}

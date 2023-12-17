package com.potatoes.cg.notice.domain;

import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.notice.domain.type.NoticeOptionType;
import com.potatoes.cg.notice.domain.type.NoticeStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.potatoes.cg.notice.domain.type.NoticeStatusType.USABLE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "TBL_NOTICE")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Notice {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long noticeCode;

    @Column(nullable = false)
    private String noticeTitle;

    @Column(nullable = false)
    private String noticeBody;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime noticeRegisterDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime noticeModifyDate;

    @Column
    private LocalDateTime noticeDeleteDate;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private NoticeStatusType noticeStatus = USABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberCode")
    private Member memberCode;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private NoticeOptionType noticeOption;

}
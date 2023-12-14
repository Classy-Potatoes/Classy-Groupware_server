package com.potatoes.cg.calendar.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.calendar.domain.type.StatusType;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.potatoes.cg.calendar.domain.type.StatusType.PROGRESS;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_calendar")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tbl_calendar SET status = 'DELETED' WHERE calendar_code = ?")
public class Calendar {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long calendarCode;

    @Column(nullable = false)
    private String calendarTitle;

    @Column
    private String calendarContent;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime calendarCreatedDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime calendarModifiedDate;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private StatusType status = PROGRESS;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(nullable = false)
    private LocalDateTime calendarStartedDate;

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(nullable = false)
    private LocalDateTime calendarEndDate;

        @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberCode")
    private Member member;

//    @Column(nullable = false)
//    private int memberCode;

    public Calendar(String calendarTitle, String calendarContent, LocalDateTime calendarStartedDate, LocalDateTime calendarEndDate, Member memberCode) {
        this.calendarTitle = calendarTitle;
        this.calendarContent = calendarContent;
        this.calendarStartedDate = calendarStartedDate;
        this.calendarEndDate = calendarEndDate;
        this.member = memberCode;
    }

    public static Calendar of(String calendarTitle, String calendarContent, LocalDateTime calendarStartedDate, LocalDateTime calendarEndDate, Member memberCode) {

        return new Calendar(
                calendarTitle,
                calendarContent,
                calendarStartedDate,
                calendarEndDate,
                memberCode
        );
    }

    public void update(String calendarTitle, String calendarContent, LocalDateTime calendarStartedDate, LocalDateTime calendarEndDate) {
        this.calendarTitle = calendarTitle;
        this.calendarContent = calendarContent;
        this.calendarStartedDate = calendarStartedDate;
        this.calendarEndDate = calendarEndDate;
    }
}

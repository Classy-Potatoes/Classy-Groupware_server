package com.potatoes.cg.project.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.project.domain.type.ProjectStatusType;
import com.potatoes.cg.project.domain.type.ProjectOptionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.potatoes.cg.project.domain.type.ProjectOptionType.*;
import static com.potatoes.cg.project.domain.type.ProjectStatusType.USABLE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_project_reply")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tbl_project_reply SET reply_state = 'DELETED' WHERE reply_code = ?")
public class ProjectReply {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long replyCode;

    @Column(nullable = false)
    private String replyBody;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime replyCreatedDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime replyModifyDate;


    @Enumerated(value = STRING)
    @Column(nullable = false)
    private ProjectStatusType replyState = USABLE;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private ProjectOptionType replyOption;

    @ManyToOne
    @JoinColumn(name = "memberCode")
    private MemberInfo member;

    @Column
    private Long postCode;

    @Column
    private Long taskCode;

    @Column
    private Long scheduleCode;

    @Column
    private Long todoListCode;


    public ProjectReply(String replyBody, MemberInfo member, Long fkCode, ProjectOptionType replyOption) {
        this.replyBody = replyBody;
        this.member = member;
        this.replyOption = replyOption;
        if (replyOption == POST) {
            this.postCode = fkCode;
        } else if (replyOption == TASK) {
            this.taskCode = fkCode;
        } else if (replyOption == SCHEDULE) {
            this.scheduleCode = fkCode;
        } else if (replyOption == TODO) {
            this.todoListCode = fkCode;
        }
    }


    public static ProjectReply of(final Long fkCode,
                                  final MemberInfo member, final String replyBody, final ProjectOptionType replyOption) {
        return new ProjectReply(
                replyBody,
                member,
                fkCode,
                replyOption
        );
    }

    public void update(String replyBody) {

        this.replyBody = replyBody;
    }
}

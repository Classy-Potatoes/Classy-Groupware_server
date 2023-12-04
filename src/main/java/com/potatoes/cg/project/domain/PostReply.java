package com.potatoes.cg.project.domain;

import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.project.domain.type.ProjectStatusType;
import com.potatoes.cg.project.domain.type.ReplyOptionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.potatoes.cg.project.domain.type.ProjectStatusType.USABLE;
import static com.potatoes.cg.project.domain.type.ReplyOptionType.POST;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_project_reply")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tbl_project_reply SET reply_state = 'DELETED' WHERE reply_code = ?")
public class PostReply {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long replyCode;

    @Column(nullable = false)
    private String replyBody;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime replyCreatedDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime replyModifyDate;

    @Column(nullable = false)
    private LocalDateTime replyDeleteDate;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private ProjectStatusType replyState = USABLE;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private ReplyOptionType replyOption = POST;

    @ManyToOne
    @JoinColumn(name = "memberCode")
    private MemberInfo member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postCode")
    private ProjectPost post;

    public PostReply(String replyBody, MemberInfo member, ProjectPost post ) {
        this.replyBody = replyBody;
        this.member = member;
        this.post = post;
    }

    public static PostReply of(final ProjectPost post, final MemberInfo member,
                               final String replyBody) {
        return new PostReply(
                replyBody,
                member,
                post
        );
    }
}

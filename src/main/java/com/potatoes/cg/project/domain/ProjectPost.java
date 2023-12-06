package com.potatoes.cg.project.domain;

import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.project.domain.type.ProjectStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.potatoes.cg.project.domain.type.ProjectStatusType.USABLE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_project_post")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tbl_project_post SET post_status = 'DELETED' WHERE post_code = ?")
public class ProjectPost {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long postCode;

    @Column(nullable = false)
    private String postTitle;

    @Column(nullable = false)
    private String postBody;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime postCreatedDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime postModifyDate;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private ProjectStatusType postStatus = USABLE;

    @ManyToOne
    @JoinColumn(name = "memberCode")
    private MemberInfo member;

    @Column
    private Long projectCode;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "postCode")
    private List<ProjectFile> fileEntity;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "postCode")
    private List<ProjectReply> replies;

//    @OneToMany(mappedBy = "projectPost", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PostFile> fileEntity = new ArrayList<>();


    public ProjectPost(String postTitle, String postBody,  MemberInfo memberCode, Long projectCode,
                       List<ProjectFile> fileEntity) {
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.member = memberCode;
        this.projectCode = projectCode;
        this.fileEntity = fileEntity;
    }

    public static ProjectPost of(final Long project, final MemberInfo member,
                                 final String postTitle, final String postBody, final List<ProjectFile> fileEntity) {

        return new ProjectPost(
                postTitle,
                postBody,
                member,
                project,
                fileEntity
        );
    }

    public void update(String postTitle, String postBody) {
        this.postTitle = postTitle;
        this.postBody = postBody;
    }
}

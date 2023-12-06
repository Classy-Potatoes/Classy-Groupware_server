package com.potatoes.cg.project.domain.repository;

import com.potatoes.cg.project.domain.ProjectReply;
import com.potatoes.cg.project.domain.type.ProjectStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectReplyRepository extends JpaRepository<ProjectReply, Long> {

    /* 댓글 조회 */
    Page<ProjectReply> findByPostCodeAndReplyStateNot(Long postCode, Pageable pageable, ProjectStatusType projectStatusType);

    /* 댓글 수정 */
    Optional<ProjectReply> findByReplyCodeAndReplyStateNot(Long replyCode, ProjectStatusType projectStatusType);
}

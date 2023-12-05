package com.potatoes.cg.project.domain.repository;

import com.potatoes.cg.project.domain.PostReply;
import com.potatoes.cg.project.domain.type.ProjectStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostReplyRepository extends JpaRepository<PostReply, Long> {

    /* 댓글 조회 */
    Page<PostReply> findByPostPostCodeAndReplyStateNot(Long postCode, Pageable pageable, ProjectStatusType projectStatusType);

    /* 댓글 수정 */
    Optional<PostReply> findByReplyCodeAndReplyStateNot(Long replyCode, ProjectStatusType projectStatusType);
}

package com.potatoes.cg.project.domain.repository;

import com.potatoes.cg.project.domain.PostReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectReplyRepository extends JpaRepository<PostReply, Long> {
}

package com.potatoes.cg.project.domain.repository;

import com.potatoes.cg.project.domain.ProjectPost;
import com.potatoes.cg.project.domain.type.ProjectStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectPostRepository extends JpaRepository<ProjectPost, Long> {

    /* 게시글 디테일 */
    Optional<ProjectPost> findByPostCodeAndPostStatus(Long postCode, ProjectStatusType projectStatusType );

    /* 게시글 수정 */
    Optional<ProjectPost> findByPostCodeAndPostStatusNot(Long postCode, ProjectStatusType projectStatusType);


    Page<ProjectPost> findByProjectProjectCodeAndPostStatus(Long projectCode, Pageable pageable, ProjectStatusType projectStatusType);
}

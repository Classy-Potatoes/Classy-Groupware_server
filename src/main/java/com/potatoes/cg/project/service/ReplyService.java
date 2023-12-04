package com.potatoes.cg.project.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.project.domain.ProjectPost;
import com.potatoes.cg.project.domain.PostReply;
import com.potatoes.cg.project.domain.repository.ProjectPostRepository;
import com.potatoes.cg.project.domain.repository.ProjectReplyRepository;
import com.potatoes.cg.project.dto.request.ProjectReplyCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.potatoes.cg.common.exception.type.ExceptionCode.NOT_FOUND_INFO_CODE;
import static com.potatoes.cg.common.exception.type.ExceptionCode.NOT_FOUND_POST_CODE;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final InfoRepository infoRepository;
    private final ProjectPostRepository projectPostRepository;
    private final ProjectReplyRepository projectReplyRepository;

    /* 프로젝트 일반 게시글 댓글 */
    public Long replySave(final ProjectReplyCreateRequest projectReplyCreateRequest, final CustomUser customUser) {

        MemberInfo member = infoRepository.findById(customUser.getInfoCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_INFO_CODE));

        ProjectPost post = projectPostRepository.findById(projectReplyCreateRequest.getPostCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_POST_CODE));

        final PostReply newPostReply = PostReply.of(
                post,
                member,
                projectReplyCreateRequest.getReplyBody()
        );


        final PostReply postReply = projectReplyRepository.save(newPostReply);

        return postReply.getReplyCode();
    }
}

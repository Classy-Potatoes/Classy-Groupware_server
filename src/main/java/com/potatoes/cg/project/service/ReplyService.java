package com.potatoes.cg.project.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.project.domain.ProjectPost;
import com.potatoes.cg.project.domain.PostReply;
import com.potatoes.cg.project.domain.repository.ProjectPostRepository;
import com.potatoes.cg.project.domain.repository.PostReplyRepository;
import com.potatoes.cg.project.dto.request.PostReplyCreateRequest;
import com.potatoes.cg.project.dto.request.ReplyUpdateRequest;
import com.potatoes.cg.project.dto.response.PostReplyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.potatoes.cg.common.exception.type.ExceptionCode.*;
import static com.potatoes.cg.project.domain.type.ProjectStatusType.DELETED;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final InfoRepository infoRepository;
    private final ProjectPostRepository projectPostRepository;
    private final PostReplyRepository postReplyRepository;

    /* 프로젝트 일반 게시글 댓글 */
    public Long replySave(final PostReplyCreateRequest postReplyCreateRequest, final CustomUser customUser) {

        MemberInfo member = infoRepository.findById(customUser.getInfoCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_INFO_CODE));

        ProjectPost post = projectPostRepository.findById(postReplyCreateRequest.getPostCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_POST_CODE));

        final PostReply newPostReply = PostReply.of(
                post,
                member,
                postReplyCreateRequest.getReplyBody()
        );


        final PostReply postReply = postReplyRepository.save(newPostReply);

        return postReply.getReplyCode();
    }

    /* 댓글 삭제 */
    public void postReplyDelete(final Long replyCode) {
        postReplyRepository.deleteById(replyCode);
    }

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("replyCode").descending());
    }

    /* 댓글 조회 */
    @Transactional(readOnly = true)
    public Page<PostReplyResponse> getPostReply(final Integer page, final Long postCode) {

        // 해당 게시글에 속한 댓글을 페이지별로 가져옵니다.
        Page<PostReply> replies = postReplyRepository.findByPostPostCodeAndReplyStateNot(postCode, getPageable(page), DELETED);

        // 댓글 엔티티를 PostReplyResponse로 변환하여 반환합니다.
        return replies.map(reply -> PostReplyResponse.from(reply));
    }

    /* 댓글 수정 */
    public void postReplyUpdate(Long replyCode, ReplyUpdateRequest replyRequest) {

        PostReply reply = postReplyRepository.findByReplyCodeAndReplyStateNot(replyCode, DELETED)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_REPLY_CODE));

        reply.update(
                replyRequest.getReplyBody()
        );
    }
}

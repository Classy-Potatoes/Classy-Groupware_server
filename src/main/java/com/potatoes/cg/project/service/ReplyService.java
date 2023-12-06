package com.potatoes.cg.project.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.project.domain.ProjectPost;
import com.potatoes.cg.project.domain.ProjectReply;
import com.potatoes.cg.project.domain.ProjectTask;
import com.potatoes.cg.project.domain.repository.ProjectPostRepository;
import com.potatoes.cg.project.domain.repository.ProjectReplyRepository;
import com.potatoes.cg.project.domain.repository.ProjectTaskRepository;
import com.potatoes.cg.project.domain.type.ProjectOptionType;
import com.potatoes.cg.project.dto.request.ReplyCreateRequest;
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
    private final ProjectReplyRepository projectReplyRepository;
    private final ProjectTaskRepository projectTaskRepository;

    /* 프로젝트 일반 게시글 댓글 */
    public Long postReplySave(final ReplyCreateRequest replyCreateRequest, final CustomUser customUser) {

        MemberInfo member = infoRepository.getReferenceById(customUser.getInfoCode());

        ProjectPost post = projectPostRepository.getReferenceById(replyCreateRequest.getPostCode());

        final ProjectReply newProjectReply = ProjectReply.of(
                post.getPostCode(),
                member,
                replyCreateRequest.getReplyBody(),
                ProjectOptionType.POST
        );


        final ProjectReply projectReply = projectReplyRepository.save(newProjectReply);

        return projectReply.getReplyCode();
    }

    /* 댓글 삭제 */
    public void postReplyDelete(final Long replyCode) {
        projectReplyRepository.deleteById(replyCode);
    }

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("replyCode").descending());
    }

    /* 댓글 조회 */
    @Transactional(readOnly = true)
    public Page<PostReplyResponse> getPostReply(final Integer page, final Long postCode) {

        // 해당 게시글에 속한 댓글을 페이지별로 가져옵니다.
        Page<ProjectReply> replies = projectReplyRepository.findByPostCodeAndReplyStateNot(postCode, getPageable(page), DELETED);

        // 댓글 엔티티를 PostReplyResponse로 변환하여 반환합니다.
        return replies.map(reply -> PostReplyResponse.from(reply));
    }

    /* 댓글 수정 */
    public void postReplyUpdate(Long replyCode, ReplyUpdateRequest replyRequest) {

        ProjectReply reply = projectReplyRepository.findByReplyCodeAndReplyStateNot(replyCode, DELETED)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_REPLY_CODE));

        reply.update(
                replyRequest.getReplyBody()
        );
    }

    /* 프로젝트 업무 댓글 작성 */
    public Long taskReplySave(ReplyCreateRequest replyCreateRequest, CustomUser customUser) {

        MemberInfo member = infoRepository.getReferenceById(customUser.getInfoCode());

        ProjectTask task = projectTaskRepository.getReferenceById(replyCreateRequest.getTaskCode());

        final ProjectReply newProjectReply = ProjectReply.of(
                task.getTaskCode(),
                member,
                replyCreateRequest.getReplyBody(),
                ProjectOptionType.TASK
        );


        final ProjectReply projectReply = projectReplyRepository.save(newProjectReply);

        return projectReply.getReplyCode();
    }
}

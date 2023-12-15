package com.potatoes.cg.project.service;

import com.potatoes.cg.approval.domain.repository.FileEntityRepository;
import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.common.util.MultipleFileUploadUtils;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.project.domain.ProjectFile;
import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.ProjectPost;
import com.potatoes.cg.project.domain.ProjectReply;
import com.potatoes.cg.project.domain.repository.ProjectPostRepository;
import com.potatoes.cg.project.domain.repository.ProjectReplyRepository;
import com.potatoes.cg.project.domain.repository.ProjectRepository;
import com.potatoes.cg.project.domain.type.ProjectFileType;
import com.potatoes.cg.project.dto.request.PostUpdateRequest;
import com.potatoes.cg.project.dto.request.ProjectPostCreateRequest;
import com.potatoes.cg.project.dto.response.ProjectPostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.potatoes.cg.common.exception.type.ExceptionCode.*;
import static com.potatoes.cg.project.domain.type.ProjectStatusType.DELETED;
import static com.potatoes.cg.project.domain.type.ProjectStatusType.USABLE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final ProjectPostRepository projectPostRepository;
    private final ProjectRepository projectRepository;
    private final InfoRepository infoRepository;
    private final FileEntityRepository fileEntityRepository;
    private final ProjectReplyRepository projectReplyRepository;

    @Value("${file.projectpost-url}")
    private String PROJECTPOST_URL;
    @Value("${file.projectpost-dir}")
    private String PROJECTPOST_DIR;

    private String getRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    /* 프로젝트 일반 글 작성*/
    public Long postSave(final ProjectPostCreateRequest projectPostRequest, final CustomUser customUser,
                         final List<MultipartFile> attachment) {

        List<ProjectFile> files = new ArrayList<>();

        // 파일이 제공되었는지 확인
        if (attachment != null && !attachment.isEmpty()) {
            /* 전달된 파일을 서버의 지정 경로에 저장 */
            List<String> replaceFileNames = MultipleFileUploadUtils.saveFiles(PROJECTPOST_DIR, attachment);

            /* 파일 정보 저장 */
            for (String replaceFileName : replaceFileNames) {
                ProjectFile fileEntity = new ProjectFile(
                        replaceFileName,
                        PROJECTPOST_URL + replaceFileName,
                        getRandomName(),
                        getFileExtension(replaceFileName),
                        ProjectFileType.POST
                );
                files.add(fileEntity);
            }
        }

        MemberInfo member = infoRepository.getReferenceById(customUser.getInfoCode());

        final ProjectPost newProjectPost = ProjectPost.of(
                projectPostRequest.getProjectCode(),
                member,
                projectPostRequest.getPostTitle(),
                projectPostRequest.getPostBody(),
                files
        );

        final ProjectPost projectPost = projectPostRepository.save(newProjectPost);

        return projectPost.getPostCode();
    }

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("postCode").descending());
    }


    /* 프로젝트 일반 글 조회 */
    @Transactional(readOnly = true)
    public Page<ProjectPostResponse> getPostsDetail(final Integer page, final Long projectCode, final CustomUser customUser) {

        Page<ProjectPost> posts = projectPostRepository.findByProjectCodeAndPostStatus(projectCode, getPageable(page),  USABLE );


        return posts.map(post -> {
            List<ProjectReply> replies = post.getReplies(); // 댓글을 즉시 가져오기
            List<ProjectFile> files = post.getFileEntity();
            return ProjectPostResponse.from(post, replies, files, customUser);
        });
    }

    /* 프로젝트 일반 글 삭제 */
    public void postDelete(final Long postCode) {
        projectPostRepository.deleteById(postCode);
    }


    /* 프로젝트 일반 글 수정 */
    public void postUpdate(final Long postCode, final PostUpdateRequest postUpdateRequest) {

        ProjectPost post = projectPostRepository.findByPostCodeAndPostStatusNot(postCode, DELETED)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_POST_CODE));


        post.update(
                postUpdateRequest.getPostTitle(),
                postUpdateRequest.getPostBody()
        );

    }

}

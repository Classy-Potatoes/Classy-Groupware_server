package com.potatoes.cg.project.service;

import com.potatoes.cg.approval.domain.repository.FileEntityRepository;
import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.common.util.FileUploadUtils;
import com.potatoes.cg.common.util.MultipleFileUploadUtils;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.project.domain.PostFile;
import com.potatoes.cg.project.domain.PostReply;
import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.ProjectPost;
import com.potatoes.cg.project.domain.repository.ProjectPostRepository;
import com.potatoes.cg.project.domain.repository.ProjectRepository;
import com.potatoes.cg.project.dto.request.PostUpdateRequest;
import com.potatoes.cg.project.dto.request.ProjectPostCreateRequest;
import com.potatoes.cg.project.dto.response.PostReplyResponse;
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

        /* 전달 된 파일을 서버의 지정 경로에 저장 */
        List<String> replaceFileNames = MultipleFileUploadUtils.saveFiles(PROJECTPOST_DIR, (attachment));

        /*파일 정보 저장 */
        List<PostFile> files = new ArrayList<>();
        for (String replaceFileName : replaceFileNames) {
            PostFile fileEntity = new PostFile(
                    replaceFileName,
                    PROJECTPOST_DIR,
                    getRandomName(),
                    getFileExtension(replaceFileName)

            );
            files.add(fileEntity);
        }

        Project project = projectRepository.findById(projectPostRequest.getProjectCode())
                .orElseThrow(() -> new NotFoundException(NOT_PROJECT_CODE));

        MemberInfo member = infoRepository.findById(customUser.getInfoCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_INFO_CODE));

        final ProjectPost newProjectPost = ProjectPost.of(
                project,
                member,
                projectPostRequest.getPostTitle(),
                projectPostRequest.getPostBody(),
                files
        );


        final ProjectPost projectPost = projectPostRepository.save(newProjectPost);

        return projectPost.getPostCode();
    }

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 10, Sort.by("postCode").descending());
    }

    /* 프로젝트 일반 글 디테일 조회 */
    @Transactional(readOnly = true)
    public ProjectPostResponse getPostDetail(final Long postCode) {

        ProjectPost post = projectPostRepository.findByPostCodeAndPostStatus(postCode, USABLE)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_POST_CODE));

        return ProjectPostResponse.from(post);

    }

    /* 프로젝트 일반 글 조회 */
    @Transactional(readOnly = true)
    public Page<ProjectPostResponse> getPostsDetail(Integer page, Long projectCode) {

        Page<ProjectPost> posts = projectPostRepository.findByProjectProjectCodeAndPostStatus(projectCode, getPageable(page),  USABLE );

        return posts.map(post -> ProjectPostResponse.from(post));
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

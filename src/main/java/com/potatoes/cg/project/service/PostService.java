package com.potatoes.cg.project.service;

import com.potatoes.cg.approval.domain.repository.FileEntityRepository;
import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.common.util.FileUploadUtils;
import com.potatoes.cg.common.util.MultipleFileUploadUtils;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.project.domain.PostFile;
import com.potatoes.cg.project.domain.Project;
import com.potatoes.cg.project.domain.ProjectPost;
import com.potatoes.cg.project.domain.repository.ProjectPostRepository;
import com.potatoes.cg.project.domain.repository.ProjectRepository;
import com.potatoes.cg.project.dto.request.PostUpdateRequest;
import com.potatoes.cg.project.dto.request.ProjectPostCreateRequest;
import com.potatoes.cg.project.dto.response.ProjectPostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    @Transactional
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

    /* 프로젝트 일반 글 조회 */
    @Transactional(readOnly = true)
    public ProjectPostResponse getPostDetail(final Long postCode) {

        ProjectPost post = projectPostRepository.findByPostCodeAndPostStatus(postCode, USABLE)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_POST_CODE));

        return ProjectPostResponse.from(post);

    }

    /* 프로젝트 일반 글 삭제 */
    public void postDelete(final Long postCode) {
        projectPostRepository.deleteById(postCode);
    }

//    /* 프로젝트 일반 글 수정 */
//    public void postUpdate(final Long postCode, final PostUpdateRequest postUpdateRequest,
//                           final List<MultipartFile> attachment, final CustomUser customUser) {
//
//        ProjectPost post = projectPostRepository.findByPostCodeAndPostStatusNot(postCode, DELETED)
//                .orElseThrow(() -> new NotFoundException(NOT_FOUND_POST_CODE));
//
//        Project project = projectRepository.findById(postUpdateRequest.getProjectCode())
//                .orElseThrow(() -> new NotFoundException(NOT_PROJECT_CODE));
//
//        MemberInfo member = infoRepository.findById(customUser.getInfoCode())
//                .orElseThrow(() -> new NotFoundException(NOT_FOUND_INFO_CODE));
//
//        /* 첨부파일 수정 */
//
//        List<MultipartFile> multipleAttachments = getMultipleAttachments(); // 여러 개의 MultipartFile을 얻는 방법에 따라 구현해야 함
//        String fileToDelete = getFileNameToDelete(); // 삭제할 파일의 이름 또는 경로에 따라 수정
//
//        for (MultipartFile attachment : multipleAttachments) {
//            if (attachment != null) {
//                /* 새로 입력된 파일 저장 */
//                String replaceFileName = FileUploadUtils.saveFile(PROJECTPOST_DIR, getRandomName(), attachment);
//
//                /* 엔터티 정보 변경 - 여러 파일에 대한 처리가 필요하다면, 엔터티를 적절히 수정해야 함 */
//                // post.updateFileEntity(PROJECTPOST_DIR + replaceFileName);
//
//                // 여러 파일에 대한 작업을 수행하는 로직 추가
//
//                // 특정 파일 삭제 로직 추가
//                if (attachment.getOriginalFilename().equals(fileToDelete)) {
//                    // 기존 파일 삭제
//                    FileUploadUtils.deleteFile(PROJECTPOST_DIR, post.getFileEntity().replaceAll(PROJECTPOST_DIR));
//                }
//
//
//          }
//        }
//    }
}

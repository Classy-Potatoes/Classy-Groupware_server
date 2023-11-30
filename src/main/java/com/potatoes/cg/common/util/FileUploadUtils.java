package com.potatoes.cg.common.util;

import com.potatoes.cg.common.exception.ServerInternalException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.potatoes.cg.common.exception.type.ExceptionCode.FAIL_TO_DELETE_FILE;
import static com.potatoes.cg.common.exception.type.ExceptionCode.FAIL_TO_UPLOAD_FILE;


public class FileUploadUtils {
    // MultipartFile 객체를 uploadDir,fileName 에 저장한다
    public static String saveFile(String uploadDir, String fileName, MultipartFile multipartFile) {

        try (InputStream inputStream = multipartFile.getInputStream()) {

            Path uploadPath = Paths.get(uploadDir);
            /* 업로드 경로가 존재하지 않을 시 경로 먼저 생성*/
            if (!Files.exists(uploadPath))
                Files.createDirectories(uploadPath);

            /* 파일명 생성*/
            String replaceFileName = fileName + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

            /* 파일 저장
             * StandardCopyOption.REPLACE_EXISTING 은 같은 파일이있으면 교체하겠다 라는 의미 */
            Path filePath = uploadPath.resolve(replaceFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            return replaceFileName;

        } catch (IOException e) {
            throw new ServerInternalException(FAIL_TO_UPLOAD_FILE);// import 처리함

        }
    }

    public static void deleteFile(String uploadDir, String fileName) {
            /* 새롭게 이미지가 업데이트되면 기존 파일은 삭제를 하기위해 필요한 로직 */
        try {
            Path uploadPath = Paths.get(uploadDir);
            Path filePath = uploadPath.resolve(fileName);
            Files.delete(filePath);
        } catch (IOException e) {
            throw new ServerInternalException(FAIL_TO_DELETE_FILE);
        }
    }
}

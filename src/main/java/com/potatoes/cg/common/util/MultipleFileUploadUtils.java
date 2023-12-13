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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.potatoes.cg.common.exception.type.ExceptionCode.FAIL_TO_DELETE_FILE;
import static com.potatoes.cg.common.exception.type.ExceptionCode.FAIL_TO_UPLOAD_FILE;


public class MultipleFileUploadUtils {

    // MultipartFile 객체를 uploadDir,fileName 에 저장한다
    public static List<String> saveFiles(String uploadDir, List<MultipartFile> multipartFiles) {
        List<String> fileNames = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileNameWithExtension = multipartFile.getOriginalFilename();
                String fileName = FilenameUtils.removeExtension(fileNameWithExtension);
                String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                String replaceFileName = fileName + "." + fileExtension;

                Path filePath = uploadPath.resolve(replaceFileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

                fileNames.add(replaceFileName);
            } catch (IOException e) {
                throw new ServerInternalException(FAIL_TO_UPLOAD_FILE);
            }
        }

        return fileNames;
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

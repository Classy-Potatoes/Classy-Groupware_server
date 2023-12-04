package com.potatoes.cg.member.domain;

import com.potatoes.cg.approval.domain.type.ApprovalFileType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

import static com.potatoes.cg.approval.domain.type.ApprovalFileType.APPROVAL;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_file")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileCode;

    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String filePathName;
    @Column(nullable = false)
    private String fileSaveName;
    @Column(nullable = false)
    private String fileExtName;
    @Column(nullable = false)
    private String fileType = "PROFILE_IMAGE";

    private Long memberCode;


    public ProfileImage(String replaceFileName, String filePathName, String randomName,
                        String fileExtension) {
        this.fileName = replaceFileName;
        this.filePathName = filePathName;
        this.fileSaveName = randomName;
        this.fileExtName = fileExtension;
    }

    public static ProfileImage of(String fileName, String filePathName, String fileSaveName,
                                  String fileExtName) {
        return new ProfileImage(
                fileName,
                filePathName,
                fileSaveName,
                fileExtName
        );
    }

    public void updateImage( String fileName, String filePathName, String fileSaveName,
                             String fileExtName ) {
        this.fileName = fileName;
        this.filePathName = filePathName;
        this.fileSaveName = fileSaveName;
        this.fileExtName = fileExtName;
    }

}

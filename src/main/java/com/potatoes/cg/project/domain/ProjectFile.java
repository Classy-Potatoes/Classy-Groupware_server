package com.potatoes.cg.project.domain;

import com.potatoes.cg.project.domain.type.ProjectFileType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.potatoes.cg.project.domain.type.ProjectFileType.POST;
import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "tbl_file")
@NoArgsConstructor
@Getter
public class ProjectFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileCode;

    @Column
    private String fileName;
    @Column
    private String filePathName;
    @Column
    private String fileSaveName;
    @Column
    private String fileExtName;

    @Column(name = "fileType")
    @Enumerated(value = STRING)
    private ProjectFileType projectFileType;


    public ProjectFile(String replaceFileName, String filePathName, String randomName, String fileExtension, ProjectFileType projectFileType) {
        this.fileName = replaceFileName;
        this.filePathName = filePathName;
        this.fileSaveName = randomName;
        this.fileExtName = fileExtension;
        this.projectFileType = projectFileType;
    }


}

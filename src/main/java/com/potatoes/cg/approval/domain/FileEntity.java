package com.potatoes.cg.approval.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tbl_file")
@NoArgsConstructor
@Getter
public class FileEntity {

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
    private String extName;
//    @ManyToOne
//    @JoinColumn(name = "approvalCode")
//    private String fileType;



}

package com.potatoes.cg.projectSchedule.domain;

import com.potatoes.cg.projectSchedule.domain.type.ScheduleFileType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "tbl_file")
@NoArgsConstructor
@Getter
public class ScheduleFile {

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
    private ScheduleFileType scheduleFileType;
}

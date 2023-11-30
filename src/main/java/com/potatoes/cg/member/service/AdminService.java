package com.potatoes.cg.member.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.member.domain.Dept;
import com.potatoes.cg.member.domain.Job;
import com.potatoes.cg.member.domain.MemberInfoModify;
import com.potatoes.cg.member.domain.MemberInfoSelect;
import com.potatoes.cg.member.domain.repository.DeptRepository;
import com.potatoes.cg.member.domain.repository.InfoModifyRepository;
import com.potatoes.cg.member.domain.repository.InfoSelectRepository;
import com.potatoes.cg.member.domain.repository.JobRepository;
import com.potatoes.cg.member.dto.request.InfoRegistRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.potatoes.cg.common.exception.type.ExceptionCode.NOT_FOUND_DEPT_CODE;
import static com.potatoes.cg.common.exception.type.ExceptionCode.NOT_FOUND_JOB_CODE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final InfoSelectRepository infoSelectRepository;
    private final InfoModifyRepository infoModifyRepository;
    private final JobRepository jobRepository;
    private final DeptRepository deptRepository;



    /* 사전 회원정보 등록 */
    public void infoRegist( final InfoRegistRequest infoRequest ) {

        final MemberInfoModify newMemberInfoModify = MemberInfoModify.of(
                infoRequest.getInfoName(),
                infoRequest.getJobCode(),
                infoRequest.getDeptCode(),
                "blank",
                "blank",
                0L,
                "blank",
                "blank"
        );

        infoModifyRepository.save( newMemberInfoModify );

    }


}

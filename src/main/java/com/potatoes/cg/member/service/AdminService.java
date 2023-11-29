package com.potatoes.cg.member.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.member.domain.Dept;
import com.potatoes.cg.member.domain.Job;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.DeptRepository;
import com.potatoes.cg.member.domain.repository.InfoRepository;
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

    private final InfoRepository infoRepository;
    private final JobRepository jobRepository;
    private final DeptRepository deptRepository;



    /* 사전 회원정보 등록 */
    public void infoRegist( final InfoRegistRequest infoRequest ) {

        log.info("----------1111 : {}", infoRequest);

        // 직급 코드 검증
        Job job = jobRepository.findById( infoRequest.getJobCode() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_JOB_CODE ) );

        log.info("----------22222 : {}", job);

        // 부서 코드 검증
        Dept dept = deptRepository.findById( infoRequest.getDeptCode() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_DEPT_CODE ) );

        log.info("----------3333 : {}", dept);

        final MemberInfo newMemberInfo = MemberInfo.of(
                infoRequest.getInfoName(),
                job,
                dept,
                "blank",
                "blank",
                0L,
                "blank",
                "blank"
        );

        infoRepository.save( newMemberInfo );

    }


}

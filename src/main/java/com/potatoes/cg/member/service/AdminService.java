package com.potatoes.cg.member.service;

import com.potatoes.cg.approval.domain.Reference;
import com.potatoes.cg.member.domain.Dept;
import com.potatoes.cg.member.domain.History;
import com.potatoes.cg.member.domain.Job;
import com.potatoes.cg.member.domain.MemberInfo;
import com.potatoes.cg.member.domain.repository.DeptRepository;
import com.potatoes.cg.member.domain.repository.HistoryRepository;
import com.potatoes.cg.member.domain.repository.InfoRepository;
import com.potatoes.cg.member.domain.repository.JobRepository;
import com.potatoes.cg.member.dto.request.InfoRegistRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        Dept dept = deptRepository.getReferenceById( infoRequest.getDeptCode() );
        Job job = jobRepository.getReferenceById( infoRequest.getJobCode() );

        // 활동 이력 추가, infoCode는 insert후 영속성전이로 업데이트.
        final List<History> newHistory = new ArrayList<>();
        newHistory.add( History.of(
                "입사",
                dept,
                job,
                "최초입사"
        ));

        // 사전등록, 활동이력도 동시에 insert
        final MemberInfo newMemberInfo = MemberInfo.of(
                infoRequest.getInfoName(),
                dept,
                job,
                "blank",
                "blank",
                0L,
                "blank",
                "blank",
                newHistory
        );

        infoRepository.save( newMemberInfo );

    }


}

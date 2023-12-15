package com.potatoes.cg.member.service;

import com.potatoes.cg.member.domain.*;
import com.potatoes.cg.member.domain.repository.*;
import com.potatoes.cg.member.dto.request.InfoRegistRequest;
import com.potatoes.cg.member.dto.response.AdminMembersResponse;
import com.potatoes.cg.member.dto.response.NonMembersResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.potatoes.cg.member.domain.type.MemberInfoStatus.NONREGIST;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final InfoRepository infoRepository;
    private final MemberRepository memberRepository;
    private final JobRepository jobRepository;
    private final DeptRepository deptRepository;


    private Pageable getPageableNon(final Integer page) {
        return PageRequest.of(page - 1, 8, Sort.by("infoCode").descending());
    }

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 8, Sort.by("memberInfo.infoName"));
    }

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

    /* 회원 목록 조회(관리자) */
    @Transactional(readOnly = true)
    public Page<AdminMembersResponse> getAdminMembers( Integer page ) {

        Page<Member> memberList = memberRepository.findAll( getPageable( page ) );

        return memberList.map( member -> AdminMembersResponse.from( member ) );
    }


    /* 회원 목록 조회(관리자, search) */
    @Transactional(readOnly = true)
    public Page<AdminMembersResponse> getAdminMembersByInfoName( Integer page, String infoName ) {

        Page<Member> memberList = memberRepository.findByMemberInfoInfoNameContains( getPageable( page ), infoName );

        return memberList.map( member -> AdminMembersResponse.from( member ) );
    }


    /* 미분류 회원 목록 조회 */
    @Transactional(readOnly = true)
    public Page<NonMembersResponse> getNonMembers( Integer page ) {

        Page<MemberInfo> nonMemberList = infoRepository.findByInfoStatus( getPageableNon( page ), NONREGIST );

        return nonMemberList.map( nonMember -> NonMembersResponse.from( nonMember ) );
    }


    /* 미분류 회원 목록 조회(search) */
    @Transactional(readOnly = true)
    public Page<NonMembersResponse> getNonMembersByInfoName( Integer page, String infoName ) {

        Page<MemberInfo> nonMemberList = infoRepository.findByInfoNameContainsAndInfoStatus( getPageableNon( page ), infoName, NONREGIST );

        return nonMemberList.map( nonMember -> NonMembersResponse.from( nonMember ) );
    }


}

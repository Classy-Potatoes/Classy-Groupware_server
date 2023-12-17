package com.potatoes.cg.member.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.common.exception.type.ExceptionCode;
import com.potatoes.cg.common.util.FileUploadUtils;
import com.potatoes.cg.member.domain.*;
import com.potatoes.cg.member.domain.repository.*;
import com.potatoes.cg.member.dto.request.InfoRegistRequest;
import com.potatoes.cg.member.dto.request.MemberUpdateRequest;
import com.potatoes.cg.member.dto.response.AdminMembersResponse;
import com.potatoes.cg.member.dto.response.MemberResponse;
import com.potatoes.cg.member.dto.response.NonMembersResponse;
import com.potatoes.cg.member.dto.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.potatoes.cg.common.exception.type.ExceptionCode.*;
import static com.potatoes.cg.member.domain.type.MemberInfoStatus.NONREGIST;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final InfoRepository infoRepository;
    private final JobRepository jobRepository;
    private final DeptRepository deptRepository;
    private final HistoryRepository historyRepository;
    private final ProfileImageRepository profileImageRepository;


    @Value("${image.memberImage-url}")
    private String MEMBERIMAGE_URL;

    @Value("${image.memberImage-dir}")
    private String MEMBERIMAGE_DIR;


    private Pageable getPageableNon(final Integer page) {
        return PageRequest.of(page - 1, 8, Sort.by("infoCode").descending());
    }

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 8, Sort.by("memberInfo.infoName"));
    }

    private Pageable getPageableHistory(final Integer page) {
        return PageRequest.of(page - 1, 7, Sort.by("historyDate"));
    }

    private String getRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
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


    /* 회원 상세 조회(관리자) */
    @Transactional(readOnly = true)
    public ProfileResponse getCustomAdminMember( Long memberCode ) {

        // 회원 조회
        Member member = memberRepository.findById( memberCode )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBERCODE ) );

        // 이미지 조회
        final ProfileImage profileImage = profileImageRepository.findByMemberCode( memberCode )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_CODE ));

        // 부서, 직급 목록 조회
        final List<Dept> deptList = deptRepository.findAll();
        final List<Job> jobList = jobRepository.findAll();

        // 이력 조회
        final List<History> historys = historyRepository.findByInfoCode( member.getMemberInfo().getInfoCode() );


        return ProfileResponse.from( member, profileImage, deptList, jobList, historys );
    }


    /* 회원상세 수정(관리기능) */
    public void adminProfileUpdate( final MemberUpdateRequest memberUpdateRequest,
                                    final MultipartFile newProfileImg,
                                    final Long memberCode ) {

        // 이미지 수정이 있다면 재등록
        if ( newProfileImg != null ) {

            final ProfileImage profileImage = profileImageRepository.findByMemberCode( memberCode )
                    .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_CODE ));

            String replaceFileName = FileUploadUtils.saveFile( MEMBERIMAGE_DIR, getRandomName(), newProfileImg );

            /* 기존 이미지 삭제 */
            FileUploadUtils.deleteFile( MEMBERIMAGE_DIR, profileImage.getFileSaveName() );

            profileImage.updateImage(
                    newProfileImg.getOriginalFilename(),
                    MEMBERIMAGE_URL + replaceFileName,
                    replaceFileName,
                    getFileExtension( replaceFileName )
            );
        }


        final Dept dept = deptRepository.getReferenceById( memberUpdateRequest.getDeptCode() );
        final Job job = jobRepository.getReferenceById( memberUpdateRequest.getJobCode() );
        final Member member = memberRepository.findById( memberCode )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_CODE ) );
        final MemberInfo memberInfo = infoRepository.findById( member.getMemberInfo().getInfoCode() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_INFO_CODE ));


        if ( !memberInfo.getDept().getDeptCode().equals( memberUpdateRequest.getDeptCode() ) ) {

            final History newHistory = History.updateOf(
                    "부서변동",
                    dept,
                    job,
                    "부서이동",
                    memberUpdateRequest.getInfoCode()
            );
            historyRepository.save( newHistory );
        }

        if ( !memberInfo.getJob().getJobCode().equals( memberUpdateRequest.getJobCode() ) ) {

            final History newHistory = History.updateOf(
                    "직급변동",
                    dept,
                    job,
                    "직급변동",
                    memberUpdateRequest.getInfoCode()
            );
            historyRepository.save( newHistory );
        }


        member.updateStatus( memberUpdateRequest.getMemberStatus() );

        memberInfo.update2(
                memberUpdateRequest.getInfoEmail(),
                memberUpdateRequest.getInfoPhone(),
                memberUpdateRequest.getInfoName(),
                dept,
                job,
                memberUpdateRequest.getInfoZipcode(),
                memberUpdateRequest.getInfoAddress(),
                memberUpdateRequest.getInfoAddressAdd()
        );
    }


    /* 회원 목록 조회(관리자, search) */
    @Transactional(readOnly = true)
    public Page<AdminMembersResponse> getAdminMembersByInfoName( Integer page, String infoName ) {

        Page<Member> memberList = memberRepository.findByMemberInfoInfoNameContains( getPageable( page ), infoName );

        return memberList.map( member -> AdminMembersResponse.from( member ) );
    }


    /* 미등록 회원 목록 조회 */
    @Transactional(readOnly = true)
    public Page<NonMembersResponse> getNonMembers( Integer page ) {

        Page<MemberInfo> nonMemberList = infoRepository.findByInfoStatus( getPageableNon( page ), NONREGIST );

        return nonMemberList.map( nonMember -> NonMembersResponse.from( nonMember ) );
    }

    /* 미등록 회원 목록 조회(search) */
    @Transactional(readOnly = true)
    public Page<NonMembersResponse> getNonMembersByInfoName( Integer page, String infoName ) {

        Page<MemberInfo> nonMemberList = infoRepository.findByInfoNameContainsAndInfoStatus( getPageableNon( page ), infoName, NONREGIST );

        return nonMemberList.map( nonMember -> NonMembersResponse.from( nonMember ) );
    }

    /* 미등록 회원 삭제(관리자) */
    public void delete( final Long infoCode ) {

        infoRepository.deleteById( infoCode );
    }


    /* 히스토리 삭제(관리자) */
    public void deleteHistory( final Long historyCode ) {

        historyRepository.deleteById( historyCode );
    }


}

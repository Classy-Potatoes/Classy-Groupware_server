package com.potatoes.cg.member.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.common.util.FileUploadUtils;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.*;
import com.potatoes.cg.member.domain.repository.*;
import com.potatoes.cg.member.domain.type.MemberInfoStatus;
import com.potatoes.cg.member.dto.request.*;
import com.potatoes.cg.member.dto.response.*;
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
import java.util.stream.Collectors;

import static com.potatoes.cg.common.exception.type.ExceptionCode.*;
import static com.potatoes.cg.member.domain.type.MemberInfoStatus.NONREGIST;
import static com.potatoes.cg.member.domain.type.MemberInfoStatus.REGIST;
import static com.potatoes.cg.member.domain.type.MemberStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

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


    /* infoCode(사번) 검증 */
    @Transactional(readOnly = true)
    public SearchInfoResponse infoSearch( final InfoCodeCheckRequest request ) {

        final MemberInfo searchInfo = infoRepository.findById( request.getInfoCode() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_INFO_CODE ));

        return SearchInfoResponse.from( searchInfo );

    }

    private String getRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /* 회원 계정 가입(tbl_member) */
    public void regist( final MemberSignupRequest memberRequest, final MultipartFile profileImg ) {

        // 프로필 이미지 등록 시작
        String replaceFileName = FileUploadUtils.saveFile( MEMBERIMAGE_DIR, getRandomName(), profileImg );

        final List<ProfileImage> profileImages = new ArrayList<>();
        profileImages.add( ProfileImage.of(
                profileImg.getOriginalFilename(),
                MEMBERIMAGE_URL + replaceFileName,
                replaceFileName,
                getFileExtension( replaceFileName )
        ));


        MemberInfo info = infoRepository.findById( memberRequest.getInfoCode() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_INFO_CODE ) );

        info.update(
                REGIST,
                memberRequest.getInfoEmail(),
                memberRequest.getInfoPhone(),
                memberRequest.getInfoZipcode(),
                memberRequest.getInfoAddress(),
                memberRequest.getInfoAddressAdd()
        );

        final Member newMember = Member.of(
                memberRequest.getMemberId(),
                passwordEncoder.encode( memberRequest.getMemberPassword() ),
                info,
                profileImages
        );

        memberRepository.save( newMember );

    }

    /* 아이디 찾기 */
    @Transactional(readOnly = true)
    public MemberResponse searchId( final MemberIdCheckRequest request ) {

        final Member member =
                memberRepository.findByMemberInfoInfoCodeAndMemberInfoInfoName( request.getInfoCode(), request.getInfoName() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_INFO_CODE_AND_INFO_NAME ));

        return MemberResponse.from( member );
    }

    /* 아이디 중복 검사 */
    @Transactional(readOnly = true)
    public Boolean duplicateId( final MemberDuplicateIdRequest request ) {

        return memberRepository.existsByMemberId( request.getMemberId() );
    }


    /* 비밀번호 변경(마이페이지) */
    public Boolean pwdUpdate( final MemberPwdRequest pwdSearchRequest, final Long memberCode ) {

        Member member = memberRepository.findById( memberCode )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_CODE ));

        // 현재 비밀번호 검증
        if ( passwordEncoder.matches( pwdSearchRequest.getCurrentPwd(), member.getMemberPassword() ) ) {

            member.updatePwd(
                    passwordEncoder.encode( pwdSearchRequest.getMemberPassword() )
            );

            return true;
        } else {
            return false;
        }

    }


    /* 계정 반납 */
    public void returnUser( final CustomUser customUser ) {

        Member member = memberRepository.findById( customUser.getMemberCode() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_CODE ) );

        member.returnUser(
               DELETE,
                ""
        );

        final History newHistory = History.deleteOf(
                "회원반납",
                member.getMemberInfo().getDept(),
                member.getMemberInfo().getJob(),
                "퇴사",
                customUser.getInfoCode()
        );

        historyRepository.save( newHistory );
    }



    /* 프로필 조회(회원상세조회) */
    @Transactional(readOnly = true)
    public ProfileResponse getProfile( final CustomUser customUser ) {

        // 회원 조회
        final Member member = memberRepository.findById( customUser.getMemberCode() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_CODE ));

        // 이미지 조회
        final ProfileImage profileImage = profileImageRepository.findByMemberCode( customUser.getMemberCode() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_CODE ));

        // 부서, 직급 목록 조회
        final List<Dept> deptList = deptRepository.findAll();
        final List<Job> jobList = jobRepository.findAll();

        // 이력 조회
        final List<History> historys = historyRepository.findByInfoCode( customUser.getInfoCode() );


        return ProfileResponse.from( member, profileImage, deptList, jobList, historys );
    }


    /* 회원상세 수정(마이페이지) */
    public void profileUpdate( final MemberUpdateRequest memberUpdateRequest,
                               final MultipartFile newProfileImg,
                               final CustomUser customUser ) {

        // 이미지 수정이 있다면 재등록
        if ( newProfileImg != null ) {

            final ProfileImage profileImage = profileImageRepository.findByMemberCode( customUser.getMemberCode() )
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
        final MemberInfo memberInfo = infoRepository.findById( customUser.getInfoCode() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_INFO_CODE ));
        final Member member = memberRepository.findById( customUser.getMemberCode() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_CODE ) );


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



    /* 회원 목록 조회(연락망) */
    @Transactional(readOnly = true)
    public Page<MembersResponse> memberList( final Integer page ) {

        Page<Member> memberList = memberRepository.findByMemberStatus( getPageable( page ), ACTIVE );

        return memberList.map( member -> MembersResponse.from( member ) );
    }

    /* 회원 목록 조회(연락망, search) */
    @Transactional(readOnly = true)
    public Page<MembersResponse> memberListByInfoName( final Integer page, final String infoName ) {

        Page<Member> memberList = memberRepository.findByMemberInfoInfoNameContainsAndMemberStatus( getPageable( page ), infoName, ACTIVE );

        return memberList.map( member -> MembersResponse.from( member ) );
    }


    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 8, Sort.by("memberInfo.infoName"));
    }


    /* 회원 상세 조회(연락망) */
    @Transactional(readOnly = true)
    public ProfileResponse getCustomNetworkMember( Long memberCode ) {

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

    /* ----------------------- 부서, 직급 조회 -------------------------- */

    /* 부서 리스트 조회 */
    @Transactional(readOnly = true)
    public List<DeptResponse> deptList() {

        final List<Dept> deptList = deptRepository.findAll();

        return deptList.stream().map( dept -> DeptResponse.from( dept ) ).collect( Collectors.toList() );

    }

    /* 직급 리스트 조회 */
    @Transactional(readOnly = true)
    public List<JobResponse> jobList() {

        final List<Job> jobList = jobRepository.findAll();

        return jobList.stream().map( job -> JobResponse.from( job ) ).collect( Collectors.toList() );
    }



}

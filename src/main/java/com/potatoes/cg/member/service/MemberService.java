package com.potatoes.cg.member.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.common.util.FileUploadUtils;
import com.potatoes.cg.jwt.CustomUser;
import com.potatoes.cg.member.domain.*;
import com.potatoes.cg.member.domain.repository.*;
import com.potatoes.cg.member.dto.request.*;
import com.potatoes.cg.member.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.potatoes.cg.common.exception.type.ExceptionCode.*;
import static com.potatoes.cg.member.domain.type.MemberStatus.ACTIVE;
import static com.potatoes.cg.member.domain.type.MemberStatus.DELETE;

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
                MEMBERIMAGE_DIR,
                replaceFileName,
                getFileExtension( replaceFileName )
        ));


        MemberInfo info = infoRepository.findById( memberRequest.getInfoCode() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_INFO_CODE ) );

        info.update(
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

    /* 현재 비밀번호 검증 */
    @Transactional(readOnly = true)
    public Boolean pwdSearch(final MemberPwdRequest pwdSearchRequest, final Long memberCode ) {

        Member member = memberRepository.findById( memberCode )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_CODE ));

        return passwordEncoder.matches( pwdSearchRequest.getMemberPassword(), member.getMemberPassword());
    }


    /* 비밀번호 변경(마이페이지) */
    public void pwdUpdate( final MemberPwdRequest pwdSearchRequest, final Long memberCode ) {

        Member member = memberRepository.findById( memberCode )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_CODE ));

        member.updatePwd(
                passwordEncoder.encode( pwdSearchRequest.getMemberPassword() )
        );

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
    public ProfileResponse getProfile( final Long memberCode ) {

        final Member member = memberRepository.findById( memberCode )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_CODE ));

        final ProfileImage profileImage = profileImageRepository.findByMemberCode( memberCode )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_MEMBER_CODE ));

        return ProfileResponse.from( member, profileImage );
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
                    MEMBERIMAGE_DIR,
                    replaceFileName,
                    getFileExtension( replaceFileName )
            );
        }

        // 회원정보 수정
        final MemberInfo memberInfo = infoRepository.findById( customUser.getInfoCode() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_INFO_CODE ));

        memberInfo.update(
                memberUpdateRequest.getInfoEmail(),
                memberUpdateRequest.getInfoPhone(),
                memberUpdateRequest.getInfoZipcode(),
                memberUpdateRequest.getInfoAddress(),
                memberUpdateRequest.getInfoAddressAdd()
        );

    }


    /* 회원이력 조회(마이페이지) */
    @Transactional(readOnly = true)
    public Page<HistoryResponse> myHistory( final Integer page, final CustomUser customUser ) {

        Page<History> historys = historyRepository.findByInfoCode(
                PageRequest.of(page - 1, 10, Sort.by("historyDate")),
                customUser.getInfoCode() );

        return historys.map( history -> HistoryResponse.from( history ) );
    }


    /* 회원 목록 조회 */
    @Transactional(readOnly = true)
    public Page<MemberResponse> memberList( final Integer page ) {

        Page<Member> memberList = memberRepository.findByMemberStatus(
                PageRequest.of(page - 1, 10, Sort.by("memberJoinDate")),
                ACTIVE );

        return memberList.map( member -> MemberResponse.from( member ) );
    }





    /* ----------------------- 부서, 직급 조회 -------------------------- */

    /* 부서 리스트 조회 */
    @Transactional(readOnly = true)
    public Page<DeptResponse> deptList( final Integer page ) {

        final Page<Dept> deptList = deptRepository.findAll(
                PageRequest.of(page - 1, 10, Sort.by("deptCode")));

        return deptList.map( dept -> DeptResponse.from( dept ) );
    }

    /* 직급 리스트 조회 */
    @Transactional(readOnly = true)
    public Page<JobResponse> jobList( final Integer page ) {

        final Page<Job> jobList = jobRepository.findAll(
                PageRequest.of(page - 1, 10, Sort.by("jobCode")));

        return jobList.map( job -> JobResponse.from( job ) );
    }



}

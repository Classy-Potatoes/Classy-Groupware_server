//package com.potatoes.cg.member.dto.response;
//
//import com.potatoes.cg.member.domain.Member;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//
//import java.time.LocalDateTime;
//
//import static lombok.AccessLevel.PRIVATE;
//
//@Getter
//@RequiredArgsConstructor(access = PRIVATE)
//public class DuplicateIdResponse {
//
//
//    private final String memberId;
//
//
//    public static DuplicateIdResponse from(final Member member ) {
//
//        return new DuplicateIdResponse(
//                member.getMemberCode(),
//                member.getMemberId(),
//                member.getMemberPassword(),
//                member.getMemberStatus().name(),
//                member.getMemberRole().name(),
//                member.getMemberJoinDate(),
//                member.getMemberUpdateDate(),
//                member.getMemberInfo().getInfoCode(),
//                member.getMemberInfo().getInfoName(),
//                member.getMemberInfo().getJob().getJobName(),
//                member.getMemberInfo().getDept().getDeptName(),
//                member.getMemberInfo().getInfoEmail(),
//                member.getMemberInfo().getInfoPhone(),
//                member.getMemberInfo().getInfoZipcode(),
//                member.getMemberInfo().getInfoAddress(),
//                member.getMemberInfo().getInfoAddressAdd()
//        );
//
//    }
//
//}

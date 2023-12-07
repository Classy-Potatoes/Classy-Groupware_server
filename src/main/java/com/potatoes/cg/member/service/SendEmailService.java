package com.potatoes.cg.member.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.repository.MemberRepository;
import com.potatoes.cg.member.dto.MailSendDTO;
import com.potatoes.cg.member.dto.request.MemberSearchPwdRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.potatoes.cg.common.exception.type.ExceptionCode.NOT_FOUND_INFO_CODE_AND_MEMBER_ID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SendEmailService {

    private final MemberRepository memberRepository;

    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public void createMailAndChangePwd( final MemberSearchPwdRequest request ) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        Member member = memberRepository.findByMemberInfoInfoCodeAndMemberId( request.getInfoCode(), request.getMemberId() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_INFO_CODE_AND_MEMBER_ID ));

        // 임시 비밀번호 생성과 암호화
        String str = getTempPwd();
        String authNum = passwordEncoder.encode( str );

        // 임시비밀번호로 DB 업데이트
        member.updatePwdEmail( authNum );

        try {
            helper = new MimeMessageHelper( message, true );

            String htmlContent =
                    "<div style=\"font-family: 'Nanum Gothic', Arial, sans-serif; color: white; text-align: center; background-color: #f0f0f0; padding:20px; padding-bottom: 50px;\">" +
                            "<h1 style='color: #333; font-size: 24px; margin-bottom: 10px;'>Classy Groupware 임시 비밀번호 안내</h1>" +
                            "<p style='color: #333; font-size: 16px;'>안녕하세요. Classy Groupware에서 임시 비밀번호를 안내드립니다.</p>" +
                            "<p style='color: #333; font-size: 16px;'>로그인 후 반드시 비밀번호를 변경해주세요.<br></p>" +
                            "<div style='color: white; background-color: rgb(0, 0, 0, 60%); border-radius: 10px; padding: 20px; width: 400px; margin: 0 auto;'>" +
                            "<h2 style='color: white; margin-bottom: 20px; font-size: 20px; font-weight: 300;'>임시 비밀번호: " + str + "</h2>" +
                            "<a href='http://localhost:3000/member/login' style='text-decoration: none;'>" +
                            "<button style='background-color: #2B456D; color: white; padding: 10px 20px; border: none; border-radius: 5px; font-size: 16px; cursor: pointer;'>로그인하러 가기</button></a></div></div>";


            helper.setTo( request.getInfoEmail() );
            helper.setSubject( member.getMemberInfo().getInfoName() +"님의 Classy Groupware 임시비밀번호 안내 이메일 입니다." );
            helper.setText( htmlContent, true );

            mailSender.send( message );

        } catch ( MessagingException e ) {
            throw new RuntimeException( e );
        }

    }


    // 임시 비밀번호 생성
    private String getTempPwd() {

        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String authNum = "";

        int idx = 0;

        for ( int i = 0; i < 10; i++ ) {
            idx = (int) ( charSet.length * Math.random() );
            authNum += charSet[ idx ];
        }

        return authNum;
    }


}

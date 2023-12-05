package com.potatoes.cg.member.service;

import com.potatoes.cg.common.exception.NotFoundException;
import com.potatoes.cg.member.domain.Member;
import com.potatoes.cg.member.domain.repository.MemberRepository;
import com.potatoes.cg.member.dto.request.MemberPwSendEmailRequest;
import com.potatoes.cg.member.dto.MailSendDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.potatoes.cg.common.exception.type.ExceptionCode.NOT_FOUND_INFO_CODE_AND_MEMBER_ID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SendEmailService {

    private final MemberRepository memberRepository;

    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private static final String FROM_ADDRESS = "classyGroupware";



    public MailSendDTO createMailAndChangePwd( MemberPwSendEmailRequest request ) {


        Member member = memberRepository.findByMemberInfoInfoCodeAndMemberId( request.getInputInfoCode(), request.getInputMemberId() )
                .orElseThrow( () -> new NotFoundException( NOT_FOUND_INFO_CODE_AND_MEMBER_ID ));

        // 임시 비밀번호 생성과 암호화
        String str = getTempPwd();
        String authNum = passwordEncoder.encode( str );

        MailSendDTO mailSendDTO = new MailSendDTO();

        String htmlContent = "<html>" + "<body>" +
                "<div style='max-width: 700px; margin: 20px auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>" +
                "<h1 style='color: #333;'>안녕하세요</h1>" +
                "<h1 style='color: #333;'> Classy Groupware 관련 안내 이메일입니다.</h1>" +
                "<p style='color: #555;'>[<span style='color: #007BFF;'>" + member.getMemberInfo().getInfoName() + "</span>] 님의 임시 비밀번호는 " +
                "<span style='font-weight: bold; color: #28A745;'>" + str + "</span> 입니다. </p>" +
                "<p>임시 비밀번호로 로그인하셔서 원하시는 비밀번호로 변경바랍니다.</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        mailSendDTO.setAddress( request.getInputEmail() );
        mailSendDTO.setTitle( member.getMemberInfo().getInfoName() +"님의 Classy Groupware 임시비밀번호 안내 이메일 입니다.");
        mailSendDTO.setMessage( htmlContent );

        // 임시비밀번호로 DB 업데이트
        member.updatePwdEmail( authNum );

        return mailSendDTO;
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


    // 메일 전송
    public void mailSend( MailSendDTO mailSendDTO ) {

        SimpleMailMessage message = new SimpleMailMessage();
        // 받는사람 주소
        message.setTo( mailSendDTO.getAddress() );
        // 보내는 사람 주소(설정하지 않으면 디폴트 값인 yml에 작성한 username)
        message.setFrom( SendEmailService.FROM_ADDRESS );
        //메일 제목
        message.setSubject( mailSendDTO.getTitle() );
        // 메일 내용
        message.setText( mailSendDTO.getMessage()) ;

        // 메일 전송
        mailSender.send( message );
    }


}

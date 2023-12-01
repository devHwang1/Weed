package com.example.weed.service;

import com.example.weed.dto.CustomDetails;
import com.example.weed.dto.MailDTO;
import com.example.weed.dto.UserSessionDto;
import com.example.weed.entity.Member;
import com.example.weed.repository.MemberRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.callback.PasswordCallback;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service

public class MemberService implements UserDetailsService {



    private final HttpSession session;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public MemberService(HttpSession session, MemberRepository memberRepository, PasswordEncoder passwordEncoder,JavaMailSender mailSender) {
        this.session = session;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public void save(Member.SaveRequest member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        memberRepository.save(member.toEntity());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("email"));

        UserSessionDto userSessionDto = new UserSessionDto(member);
        session.setAttribute("userSession", userSessionDto);

        return toUserDetails(member);
    }

private UserDetails toUserDetails(Member member) {
    return new CustomDetails(
            member.getEmail(),
            member.getName(),
            member.getPassword(),
           member.getDept().getDeptName(),
           member.getAuthority().getName()
    );
}
    public boolean isEmailInUse(String email) {
        return memberRepository.existsByEmail(email);
    }

    public MailDTO createMailAndChangePassword(String memberEmail) {
        String tempPassword = getTempPassword();
        String encryptedPassword = passwordEncoder.encode(tempPassword); // 임시 비밀번호 암호화

        MailDTO dto = new MailDTO();
        dto.setAddress(memberEmail);
        dto.setTitle("Weed 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. Weed 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + tempPassword + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요");

        updatePassword(encryptedPassword,memberEmail); // 암호화된 임시 비밀번호로 업데이트

        return dto;}

    //임시 비밀번호로 업데이트
//    public void updatePassword(String str, String userEmail){
//        String memberPassword = str;
//        Long memberId = memberRepository.findByEmail(userEmail).get().getId();
//        memberRepository.updatePassword(memberId,memberPassword);
//    }

    //랜덤함수로 임시비밀번호 구문 만들기
    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
    // 메일보내기
    public void mailSend(MailDTO mailDTO) {
        System.out.println("전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getAddress());
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());
        message.setFrom("comeday7741@gmail.com");
        message.setReplyTo("comeday7741@gmail.com");
        System.out.println("message"+message);
        mailSender.send(message);
    }

    //비밀번호 변경
    public void updatePassword(String str, String userEmail) {
        memberRepository.updatePassword(str, userEmail);
    }



}

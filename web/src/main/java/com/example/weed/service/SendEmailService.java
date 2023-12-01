//package com.example.weed.service;
//
//import com.example.weed.dto.MailDTO;
//import com.example.weed.entity.Member;
//import com.example.weed.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class SendEmailService {
//
//    private final MemberRepository memberRepository;
//
//    private JavaMailSender mailSender;
//    private static final String FROM_ADDRESS = "comeday7741@gmail.com";
//
//    public MailDTO createMailAndChangePassword(String userEmail){
//        String str = getTempPassword();
//        MailDTO dto = new MailDTO();
//        dto.setAddress(userEmail);
//
//        dto.setTitle(userEmail + "님의 Weed 임시비밀번호 안내 이메일 입니다");
//        dto.setMessage(userEmail + "님의 Weed 임시비밀번호 관련 이메일 입니다"+ "[" + userEmail + "]" +"님의 임시 비밀번호는 "
//                + str + " 입니다.");
//        updatePassword(str,userEmail);
//        return dto;
//
//    }
//
//
//    public String getTempPassword(){
//        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
//                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
//
//        String str = "";
//
//        int idx = 0;
//        for (int i = 0; i < 10; i++) {
//            idx = (int) (charSet.length * Math.random());
//            str += charSet[idx];
//        }
//        return str;
//    }
//
//
//    public void mailSend(MailDTO mailDto){
//        System.out.println("이멜 전송 완료!");
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(mailDto.getAddress());
//        message.setFrom(SendEmailService.FROM_ADDRESS);
//        message.setSubject(mailDto.getTitle());
//        message.setText(mailDto.getMessage());
//
//        mailSender.send(message);
//    }
//}

package com.example.weed.dto;


import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    // 메시지타입 : 입장, 채팅
    // 메시지 타입에 따라 동작하는 구조가 달라짐
    // 입장과 퇴장 시 ENTER, LEAVE 입/퇴장 이벤트 처리 가 실행
    // TALK 는 해당 내용이 채팅장을 SUB(구독) 하고 있는 모든 클라이언트들에게 전달.
    public enum MessageType {
        ENTER, TALk, LEAVE
    }
    private MessageType messageType;    //메세지타입
    private String chatRoomId;            //방번호
    private String senderId;              //보낸사람
    private String message;             //메세지
    private String messageTime;         //메세지발송시간

    //파일 업로드 관련
    private String s3DataUrl;       //파일 업로드 url
    private String fileName;        //파일 이름
    private String fileDir;         //s3 파일경로
}

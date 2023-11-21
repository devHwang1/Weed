package com.example.weed.dto;


import lombok.*;



@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    public enum MessageType {
        ENTER, TALk
    }
    private MessageType messageType;    //메세지타입
    private Long chatRoomId;            //방번호
    private Long senderId;              //보낸사람
    private String message;             //메세지
    private String messageTime;         //메세지발송시간
}

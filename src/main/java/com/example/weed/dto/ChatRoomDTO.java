package com.example.weed.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

 /*
    STOMP (Simple Text Orirented Message Protocol)
    통해 pub/sub 를 사용하면 구독자 관리가 알아서 됨
    따로 세션관리 할 코드 작성할 필요가 없어진다.
    메시지를 다른 세션의 클라이언트에게 발송하는 것도 구현 할 필요 없음
 */

public class ChatRoomDTO {

    private String roomId;      // 채팅방 아이디
    private String roomName;    // 채팅방 이름
    private int roomUserCount; // 채팅방 인원수

    private int maxUserCout;   // 채팅방 최대 인원제한
    private String roomPwd;
    private boolean secretChk; // 채팅방 잠금 여부


    // 사용자 목록을 저장하는  HashMap , 사용자의 아이디와 이름을 저장
    private HashMap<String, String> userList;


    /*
        채팅방을 생성하는 메서드
        roomName 이름을 받아 채팅방 객체를 생성하고 반환함
        생성된 채팅방은 고유한 Name과 Id 를 가짐
        UUID : Universally Unique Idendifier 범용적으로 고유한 식별자를 생성하기위한
        규칙에 따라 만들어진 문자열 중복될 가능성이 매우 낮음

    */
    /*public ChatRoomDTO create(String roomName) {
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.roomId = UUID.randomUUID().toString();
        chatRoomDTO.roomName = roomName;

        return chatRoomDTO;
    }*/

}

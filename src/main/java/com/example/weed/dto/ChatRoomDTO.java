package com.example.weed.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {

    private String roomId;     // 채팅방 아이디
    private String roomName;   // 채팅방 이름
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoomDTO(String roomId, String roomName){
        this.roomId = roomId;
        this.roomName = roomName;
    }

}

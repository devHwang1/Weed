package com.example.weed.service;

import com.example.weed.dto.ChatRoomDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Slf4j
@Data
@Service
public class ChatService {

    private final ObjectMapper mapper;
    private Map<String, ChatRoomDTO> chatRoomDTOMap;

    @PostConstruct
    private void  init() {
        chatRoomDTOMap = new LinkedHashMap<>();
    }
    public List<ChatRoomDTO> findAllRoom() {
        return new ArrayList<>(chatRoomDTOMap.values());
    }
    public ChatRoomDTO findRoomById(String roomId){
        return chatRoomDTOMap.get(roomId);
    }

    public ChatRoomDTO createRoom(String name) {
        String roomId = UUID.randomUUID().toString(); // 랜덤한 방 아이디 생성

        // Builder 를 이용해서 ChatRoom 을 Building
        ChatRoomDTO room = ChatRoomDTO.builder()
                .roomId(roomId)
                .roomName(name)
                .build();

        chatRoomDTOMap.put(roomId, room); // 랜덤 아이디와 room 정보를 Map 에 저장
        return room;
    }

}

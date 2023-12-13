package com.example.weed.controller;

import com.example.weed.dto.W1005_ChatMessageDTO;
import com.example.weed.service.W1005_ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class W1005_WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final W1005_ChatService chatService;

    @MessageMapping("/chat/{roomId}/sendMessage")
    public void sendMessage(@Payload String message, @DestinationVariable Long roomId) {
        // 채팅 메시지를 받아서 처리하는 로직
        System.out.println("Received message in chat room " + roomId + ": " + message);

        // 다시 해당 채팅방으로 메시지를 보내는 예시
        messagingTemplate.convertAndSend("/topic/chat/" + roomId + "/messages", message);
    }

}

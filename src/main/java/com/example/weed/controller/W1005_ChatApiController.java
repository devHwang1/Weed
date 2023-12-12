package com.example.weed.controller;

import com.example.weed.dto.W1005_ChatMessageDTO;
import com.example.weed.entity.ChatMessage;
import com.example.weed.entity.ChatRoom;
import com.example.weed.service.W1005_ChatRoomService;
import com.example.weed.service.W1005_ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat/{roomId}/messages")
public class W1005_ChatApiController {

    @Autowired
    private W1005_ChatRoomService chatRoomService;

    @Autowired
    private W1005_ChatService chatService;

    @GetMapping
    public List<W1005_ChatMessageDTO> getChatMessages(@PathVariable Long roomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(roomId);
        return chatRoomService.getChatMessagesByChatRoom(chatRoom)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public void sendChatMessage(@PathVariable Long roomId, @RequestParam String messageContent) {
        chatService.sendChatMessageToChatRoom(roomId, messageContent);
    }

    // ChatMessage를 DTO로 변환하는 메소드
    private W1005_ChatMessageDTO convertToDTO(ChatMessage chatMessage) {
        return new W1005_ChatMessageDTO(
                chatMessage.getId(),
                chatMessage.getContent(),
                chatMessage.getMember().getId(),
                chatMessage.getTimestamp()
        );
    }
}

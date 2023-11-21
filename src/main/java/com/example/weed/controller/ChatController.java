package com.example.weed.controller;


import com.example.weed.config.WebSocketChatHandler;
import com.example.weed.dto.ChatRoomDTO;
import com.example.weed.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {


    private final ChatService chatService;

    @PostMapping
    public ChatRoomDTO chatRoomDTO(@RequestParam String name) {
        return chatService.createRoom(name);
    }
    @GetMapping
    public List<ChatRoomDTO> findAllRooms() {
        return chatService.findAllRoom();
    }
}

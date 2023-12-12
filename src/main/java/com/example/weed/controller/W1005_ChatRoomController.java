//package com.example.weed.controller;
//
//import com.example.weed.entity.ChatRoom;
//import com.example.weed.service.W1005_ChatRoomService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.List;
//
//@Controller
//public class W1005_ChatRoomController {
//
//    @Autowired
//    private W1005_ChatRoomService chatRoomService;
//
//    @GetMapping("/chatRooms")
//    public String showChatRooms(Model model) {
//        List<ChatRoom> chatRooms = chatRoomService.getAllChatRooms();
//        model.addAttribute("chatRooms", chatRooms);
//        return "W1005_chatRooms";
//    }
//
//    @GetMapping("/chat/{roomId}")
//    public String enterChatRoom(@PathVariable Long roomId, Model model) {
//        ChatRoom chatRoom = chatRoomService.getChatRoomById(roomId);
//        model.addAttribute("chatRoom", chatRoom);
//        return "W1005_chat";
//    }
//}

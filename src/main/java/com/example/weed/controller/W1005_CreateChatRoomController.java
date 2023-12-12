//package com.example.weed.controller;
//
//import com.example.weed.service.W1005_ChatRoomService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class W1005_CreateChatRoomController {
//    @Autowired
//    private W1005_ChatRoomService chatRoomService;
//
//    @PostMapping("/createChatRoom")
//    public String createChatRoom(@RequestParam String chatRoomName) {
//        // 새 채팅방 생성 로직
//        chatRoomService.createChatRoom(chatRoomName);
//
//        // 생성된 채팅방으로 리다이렉션
//        return "redirect:/chatRooms";
//    }
//}

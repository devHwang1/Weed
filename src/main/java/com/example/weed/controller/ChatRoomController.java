package com.example.weed.controller;


import com.example.weed.dto.ChatRoomDTO;
import com.example.weed.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@AllArgsConstructor
public class ChatRoomController {


    private final ChatService chatService;

    // 채팅리스트 화면, "/" 요청을 받으면 채팅룸 리스트를 담아서 리턴
    @GetMapping("/")
    public String chatRoom(Model model) {
        model.addAttribute("list", chatService.findAllRoom());
        log.info("ChatList {}", chatService.findAllRoom());

        return "roomList";
    }

    //채팅방 생성
    //채팅방 생성후 다시 "/" 로 리턴
    @PostMapping("/chat/createRoom")
    public String createRoom(@RequestParam("roomName") String name,
                             @RequestParam("roomPwd")  String roomPwd,
                             @RequestParam("secretChk") String secretChk,
                             @RequestParam(value = "maxUserCnt", defaultValue = "2") String maxUserCnt,
                             @RequestParam("chatType") String chatType,
                                 RedirectAttributes rttr) {
       /* ChatRoomDTO chatRoom = chatService.createRoom(name);
        log.info("CreateRoom{}", chatRoom);
        rttr.addFlashAttribute("roomName", chatRoom);*/

        return "redirect:/";
    }

    //채팅방 입장화면
    // 파라미터로 넘어오는 roomID 를 확인후 해당 roomID 를 기준으로
    // 채팅방을 찾아서 클라이언트를 chatRoom 으로 보냄
    @GetMapping("/chat/room")
    public String roomDetail(Model model, String roomId) {
        log.info("roomId {}", roomId);
        model.addAttribute("room", chatService.findRoomById(roomId));

        return "chatRoom";
    }
}

package com.example.weed.controller;

import com.example.weed.dto.ChatMessageDTO;
import com.example.weed.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatController {

    private SimpMessageSendingOperations simpMessageSendingOperations;
    private ChatService chatService;


    /*
       @MessageMapping 을 통해 Socket 으로 들어오는 메시지를 발신 처리함
       이떄 클라이언트에서는 /pub/chat/message 로 요청 하게 되고 이것을 controller가 받아서 처리
       처리가 완료되면 /sub/chat/room/roomID 로메시지가 전송됨
    */
    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatMessageDTO chatMessageDTO
            , SimpMessageHeaderAccessor simpMessageHeaderAccessor) {

        //채팅방 유저 증가 시키기!
        chatService.addCountUser(chatMessageDTO.getChatRoomId());

        //채팅방에 유저 추가및 userUUID 반환
        String userUUID = chatService
                .addUserList(chatMessageDTO.getChatRoomId(), chatMessageDTO.getSenderId());

        //반환 결과를 socket 세선에 userUUID로 저장
        simpMessageHeaderAccessor.getSessionAttributes().put("userUUID", userUUID);
        simpMessageHeaderAccessor.getSessionAttributes().put("roomID", chatMessageDTO.getChatRoomId());

        chatMessageDTO.setMessage(chatMessageDTO.getSenderId() + " 님 입장 했습니다!");
        simpMessageSendingOperations.convertAndSend("/sub/chat/room"
                + chatMessageDTO.getChatRoomId(), chatMessageDTO);
    }

    //해당 유저
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessageDTO) {
        log.info("Chat [}", chatMessageDTO);
        chatMessageDTO.setMessage(chatMessageDTO.getMessage());
        simpMessageSendingOperations.convertAndSend("/sub/chat/room"
                + chatMessageDTO.getChatRoomId(), chatMessageDTO);
    }

    // 유저 퇴장 시에는 EventListener 을 통해 유저 퇴장확인
    @EventListener
    public void webSockecktDisConnectionListener(SessionDisconnectEvent sessionDisconnectEvent) {
        log.info("Disconnection {}" + sessionDisconnectEvent);

        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());

        //stomp 세션에 있던 UUID 와 roomId 를 확인해서 채팅방 유저 리스트와 room 에서해당 유저를 삭제
        String userUUID = (String) stompHeaderAccessor.getSessionAttributes().get("userUUID");
        String roomID = (String) stompHeaderAccessor.getSessionAttributes().get("roomId");

        log.info("stompHeaderAccessor {}", stompHeaderAccessor);
        // 채팅방 유저 -1 빼기
        chatService.disCountUser(roomID);

        //채팅방 유저 리스트에셔 UUID 유저 닉네임 조회 및 리스트에서 유저 삭제
        String userName = chatService.getUserName(roomID, userUUID);
        chatService.deleteUser(roomID, userUUID);

        if (userName != null) {
            log.info("User Disconnected :" + userName);

            //builder 어노테이션 활용
            ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder()
                    .messageType(ChatMessageDTO.MessageType.LEAVE)
                    .senderId(userName)
                    .message(userName + " 님 퇴장쓰!")
                    .build();

            simpMessageSendingOperations.convertAndSend("/sub/chat/room"
                    + roomID, chatMessageDTO);
        }
    }

    //채팅에 참여한 유저 리스트 반환
    @GetMapping("/chat/userList")
    @ResponseBody
    public ArrayList<String> userList(String roomId) {
        return chatService.getUserList(roomId);
    }

    //채팅에 참여한 유저 닉네임 중복확인
    @GetMapping("/chat/duplicateName")
    @ResponseBody
    public String isDuplicateName(@RequestParam("roomId") String roomId,
                                  @RequestParam("username") String username) {
        //유저 이름 확인
        String userName = chatService.isDuplicateName(roomId, username);

        log.info("동작 확인{}",userName);

        return userName;
    }
}

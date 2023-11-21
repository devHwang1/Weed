package com.example.weed.config;

import com.example.weed.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component      //스프링 컨텍스트에 등록,클래스 인스턴스관리 하고 주입
@RequiredArgsConstructor

 /*
    WebSocketChatHandler 클래스 생성하고 , TextWebSocketHandler 상속받아
    WebSocket 처리를 위한 핸들러 역할
 */
public class WebSocketChatHandler extends TextWebSocketHandler {

    //JSON 데이터를 java 객체로변 환하거나 반대로도 함.
    private final ObjectMapper mapper;

    //WebSocket 연결을 유지하는 데 사용되는 WebSocketSession 객체들을 저장하기 위한 Set
    private final Set<WebSocketSession> sessions = new HashSet<>();
    //채팅 방과 그 방에 속한 WebSocketSession 객체들을 관리하기 위한 Map
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    @Override
    //WebSocketSession이 연결될 때 실행,연결된 WebSocketSession을 sessions Set에 추가
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        log.info("{} 연결될", session.getId());

        sessions.add(session);
    }

    @Override
    /*
     handleTextMessage
     클라이언트로부터 텍스트 메시지를 받을 때 호출되는 메서드로, 메시지를 처리하는 로직이 포함.
     받은 메시지를 JSON 형식으로 읽어서 ChatMessageDTO 객체로 변환하고,
     해당 채팅 방의 WebSocketSession을 관리하는 로직을 구현
     */
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        // 클라이언트에서 전송한 메세지 를 담고 있는 내용을 WebSocket메세지에서 텍스트페이로들 가져옴
        String payload = message.getPayload();
        log.info("session {}", payload);

        // Jackson의 ObjectMapper를 사용하여 JSON 형식의 텍스트를 ChatMessageDTO 객체로 변환
        // 클라이언트가 보낸 JSON 데이터를 Java 객체로 파싱하는 과정
        ChatMessageDTO chatMessageDTO = mapper.readValue(payload, ChatMessageDTO.class);
        log.info("session {}", chatMessageDTO.toString());

        Long chatRoomId = chatMessageDTO.getChatRoomId();

        if (!chatRoomSessionMap.containsKey(chatRoomId)) {
            // 채팅 방의 식별자가 없는 경우, 새로운 채팅 방을 맵에 추가
            // 이 때, 채팅 방의 WebSocket 세션을 관리하기 위한 빈 Set을 생성하여 추가
            chatRoomSessionMap.put(chatRoomId, new HashSet<>());
        }
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

        if (chatMessageDTO.getMessageType().equals(ChatMessageDTO.MessageType.ENTER)) {
            chatRoomSession.add(session);
        }
        if (chatRoomSession.size() >= 3) {
            removeClosedSession(chatRoomSession);
        }
        sendMessageToChatRoom(chatMessageDTO, chatRoomSession);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        /*
          afterConnectionClosed 메서드: WebSocket 연결이 닫힐 때 호출되는 메서드
          WebSocketSession 객체와 연결이 닫힌 상태를 나타내는 CloseStatus가 전달
          이 메서드에서는 연결이 닫힌 WebSocketSession을 sessions Set에서 제거
         */
        log.info("{} 연결끊김", session.getId());
        sessions.remove(session);
    }

    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        /*
          removeClosedSession 메서드: 채팅 방의 세션 목록에서 연결이 닫힌 세션을 제거하는 메서드
          chatRoomSession 매개변수로 받은 세션 목록을 반복하면서 sessions Set에 포함된 세션과 일치하지 않는 세션을 제거
        */
        chatRoomSession.removeIf(sess -> !sessions.contains(sess));
    }

    private void sendMessageToChatRoom(ChatMessageDTO chatMessageDTO, Set<WebSocketSession> chatRoomSession) {
        /*
           sendMessageToChatRoom 메서드: 채팅 메시지를 채팅 방의 모든 세션에 전송하는 메서드
           chatRoomSession 매개변수로 받은 세션 목록을 병렬 스트림으로 반복하면서
           각 세션에 sendMessage 메서드를 통해 메시지를 전송
        */
        chatRoomSession.parallelStream().forEach(sess -> sendMessage(sess, chatMessageDTO));
    }


    public <T> void sendMessage(WebSocketSession session, T message) {
        /*
           sendMessage 메서드: WebSocket 세션에 메시지를 전송하는 메서드
            WebSocketSession과 전송할 메시지인 message를 받아서, mapper.writeValueAsString 메서드를 사용하여
            message 객체를 JSON 문자열로 변환한 후,
            TextMessage 객체로 래핑하여 WebSocket 세션으로 전송합니다. 예외가 발생할 경우 에러 로그를 출력
        */
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}



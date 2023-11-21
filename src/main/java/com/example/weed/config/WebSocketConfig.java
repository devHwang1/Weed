package com.example.weed.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.net.http.WebSocket;


@Configuration          //bean을 등록하고 설정
@EnableWebSocket        //WebSocket을 활성화,WebSocket 핸들러를 사용할 수 있음.
@RequiredArgsConstructor// 생성자 자동생성 webSocketHandler 대한 생성자 자동 생성

// WebSocketConfig 에 implements 함으로써 WebSocketConfigurer 인터페이스 구현
// 해서 WebSocket 관련구성을 설정하는 클래스
public class WebSocketConfig implements WebSocketConfigurer {

    // WebSocket 핸들러를 주입받는 필드, WebSocket 핸들러는 WebSocket 연결과 메시지 처리를 담당
    private final WebSocketHandler webSocketHandler;
    @Override
        /*
        registerWebSocketHandlers 메서드는
        WebSocket 의 endpoint 를 등록하는 역할을 함
        ws://주소:포트/ws/chat
        ==> endpoint 설정 : /api/v1/chat/{postId}

        "ws/chat" 경로로 WebSocket 연결을 받아들이도록 설정
        ==> ws://localhost:8099/ws/chat 으로 요청이 들어오면 websocket 통신을 진행.

        모든 cors 요청을 허용
        setAllowedOrigins("*")를 통해 모든 IP 주소에서의 연결을 허용하도록 설정(모두 접속가능)

        */
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler,"ws/chat").setAllowedOrigins("*");
    }
}

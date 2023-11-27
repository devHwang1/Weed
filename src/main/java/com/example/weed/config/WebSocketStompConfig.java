package com.example.weed.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {

    //엔드포인트 연결, 엔드포인트 통신의 도착지점을 설정함으로써, 도착후 어떠한 행위를 하게만듬
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //stomp - 점속주소 url  => ws/-stomp
        registry.addEndpoint("ws/-stomp")// 연결된 endPoint설정
                .withSockJS();                // SocketJS 를 연결한다는 설정

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //메세지를 구독하는 요청 url => 메세지를 받을때
        registry.enableSimpleBroker("/sub");

        //메세지를 발행하는 교청 url => 메세지를 보낼떄
        registry.setApplicationDestinationPrefixes("/pub");
    }
}

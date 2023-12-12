package com.example.weed.entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "m_id")
    private Member member;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Builder(builderMethodName = "hiddenBuilder")  // builderMethodName으로 기존의 builder를 숨김
    public ChatMessage(Long id, String content, Member member, LocalDateTime timestamp, ChatRoom chatRoom) {
        this.id = id;
        this.content = content;
        this.member = member;
        this.timestamp = timestamp;
        this.chatRoom = chatRoom;
    }

    public static ChatMessageBuilder builder() {
        return hiddenBuilder();
    }
}
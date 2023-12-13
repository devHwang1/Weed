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

    @ManyToOne(cascade = CascadeType.ALL)
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

    public void setRoomId(Long roomId) {
        this.chatRoom = new ChatRoom(); // 예시로 ChatRoom 객체를 생성하거나
        this.chatRoom.setId(roomId);    // 필요에 따라 적절한 설정을 수행합니다.
    }

    public void setMemberId(Long memberId) {
        this.member = new Member();     // 예시로 Member 객체를 생성하거나
        this.member.setId(memberId);    // 필요에 따라 적절한 설정을 수행합니다.
    }

}
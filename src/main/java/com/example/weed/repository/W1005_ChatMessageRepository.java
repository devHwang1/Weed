package com.example.weed.repository;

import com.example.weed.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface W1005_ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 추가적인 쿼리 메소드가 필요하다면 여기에 정의할 수 있습니다.
}


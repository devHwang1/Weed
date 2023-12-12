package com.example.weed.dto;

import lombok.*;

import java.time.LocalDateTime;
@Data
public class W1005_ChatMessageDTO {
    private Long id;
    private String content;
    private Long memberId;
    private LocalDateTime timestamp;

    public W1005_ChatMessageDTO(Long id, String content, Long memberId, LocalDateTime timestamp) {
        this.id = id;
        this.content = content;
        this.memberId = memberId;
        this.timestamp = timestamp;
    }

    public W1005_ChatMessageDTO() {
    }
}
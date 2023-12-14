package com.example.weed.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
@Data
public class W1005_ChatMessageDTO {
    private Long id;
    private String content;
    private Long memberId;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
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
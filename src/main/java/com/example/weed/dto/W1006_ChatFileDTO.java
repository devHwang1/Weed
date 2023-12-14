package com.example.weed.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class W1006_ChatFileDTO {
    private Long id;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadTime;
}
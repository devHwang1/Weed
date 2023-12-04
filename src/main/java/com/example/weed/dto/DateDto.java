package com.example.weed.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DateDto {
    // 날짜 식별자
    private Long id;
    // 출퇴근 일자
    private LocalDate date;
}

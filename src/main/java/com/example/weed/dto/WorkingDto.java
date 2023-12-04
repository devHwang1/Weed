package com.example.weed.dto;

import lombok.Data;
import java.time.LocalTime;

@Data
public class WorkingDto {
    // 날짜 식별자
    private Long id;
    // 출근 시간
    private LocalTime checkInTime;
    // 퇴근 시간
    private LocalTime checkOutTime;
    // 근무 시간
    private double workingHours;
    // 근로자 식별 정보
    private String employeeId;
}

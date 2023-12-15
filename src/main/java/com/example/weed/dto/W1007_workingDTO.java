package com.example.weed.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class W1007_workingDTO {
    private Long id;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private double workingHours;
    private String memberName;

    //생성자
    public W1007_workingDTO() {
    }

    public W1007_workingDTO(Long id, LocalDateTime checkInTime, LocalDateTime checkOutTime, double workingHours, String memberName) {
        this.id = id;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.workingHours = workingHours;
        this.memberName = memberName;
    }
}

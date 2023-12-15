package com.example.weed.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class W1007_workingDTO {
    private Long id;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String memberName;

    public W1007_workingDTO(Long id, LocalDateTime checkInTime, LocalDateTime checkOutTime, String memberName) {
        this.id = id;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.memberName = memberName;
    }
}

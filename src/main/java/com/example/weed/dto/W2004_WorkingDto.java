package com.example.weed.dto;

import com.example.weed.entity.Member;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class W2004_WorkingDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime checkInTime;

    private LocalTime checkOutTime;

    private double workingHours;

    @ManyToOne
    @JoinColumn(name = "m_id")
    private Member member;
}

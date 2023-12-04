package com.example.weed.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Working {
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

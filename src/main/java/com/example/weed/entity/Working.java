package com.example.weed.entity;

import com.example.weed.dto.W1007_workingDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
public class Working {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    private LocalDateTime checkInTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    private LocalDateTime checkOutTime;

    @ManyToOne
    @JoinColumn(name = "m_id")
    private Member member;

    public W1007_workingDTO toDTO() {
        return new W1007_workingDTO(id, checkInTime, checkOutTime, member.getName());
    }
}


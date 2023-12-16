package com.example.weed.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

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

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    @ManyToOne
    @JoinColumn(name = "m_id")
    private Member member;

    // Member를 통해 dept_name를 가져오기
    @Transient
    public String getDeptName() {
        return member != null && member.getDept() != null ? member.getDept().getDeptName() : null;
    }

}


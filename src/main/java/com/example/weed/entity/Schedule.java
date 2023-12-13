package com.example.weed.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;


@Getter
@Setter
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ScheduleId;  //스케줄 ID

    @NotBlank
    private String ScheduleTitle; //스케줄 제목

    @Temporal(TemporalType.DATE)
    private Date ScheduleStart; //시작날짜

    @Temporal(TemporalType.DATE)
    private Date ScheduleEnd; //종료날짜

    private String ScheduleColor; //스케줄 컬러

    private String ScheduleContent; //스케줄 내용

    //멤버테이블 조인
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}

package com.example.weed.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class W1004DTO {
    private Long eventId;
    private String title;
    private String content;
    private Date startDate;
    private Date endDate;
    private String color;

    //기본생성자
    public W1004DTO(){

    }

    public W1004DTO(Long eventId, String title, String content, Date startDate, Date endDate, String color){
        this.eventId = eventId;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.color = color;
    }

}

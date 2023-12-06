package com.example.weed.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class W1004EventDTO {
    private String id;
    private String title;
    private Date start;
    private Date end;
    private String color;
    private String content;
}

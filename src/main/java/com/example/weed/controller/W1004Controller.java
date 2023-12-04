package com.example.weed.controller;

import com.example.weed.dto.W1004DTO;
import com.example.weed.entity.W1004Entity;
import com.example.weed.repository.W1004Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class W1004Controller {

    @Autowired
    private W1004Repository w1004Repository;

    @GetMapping("/calendar")
    public String Calendar() {
        return "W1004";
    }

    @PostMapping("/saveW1004")
    @ResponseBody
    public String saveEvent(@RequestBody W1004DTO w1004dto) {

        // EventData를 Entity로 변환
        W1004Entity entity = new W1004Entity();
        entity.setScheduleId(w1004dto.getEventId());
        entity.setScheduleTitle(w1004dto.getTitle());
        entity.setScheduleStart(w1004dto.getStartDate());
        entity.setScheduleEnd(w1004dto.getEndDate());
        entity.setScheduleColor(w1004dto.getColor());
        entity.setScheduleContent(w1004dto.getContent());

        //db저장
        w1004Repository.save(entity);

        return "success";
    }

}

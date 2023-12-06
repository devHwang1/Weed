package com.example.weed.controller;

import com.example.weed.dto.W1001_CustomDetails;
import com.example.weed.dto.W1004DTO;
import com.example.weed.dto.W1004EventDTO;
import com.example.weed.entity.Member;
import com.example.weed.entity.W1004Entity;
import com.example.weed.repository.W1004Repository;
import com.example.weed.service.W1001_MemberService;
import com.example.weed.service.W1004service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class W1004Controller {

    @Autowired
    private W1004Repository w1004Repository;


    @Autowired
    private W1001_MemberService memberService;

    @Autowired
    private W1004service w1004service;


    @GetMapping("/calendar")
    public String calendar() {
        return "W1004";
    }


    @PostMapping("/saveW1004")
    @ResponseBody
    public String saveEvent(@RequestBody W1004DTO w1004dto) {
        W1004Entity entity = new W1004Entity();
        entity.setScheduleId(w1004dto.getEventId());
        entity.setScheduleTitle(w1004dto.getTitle());
        entity.setScheduleStart(w1004dto.getStartDate());
        entity.setScheduleEnd(w1004dto.getEndDate());
        entity.setScheduleColor(w1004dto.getColor());
        entity.setScheduleContent(w1004dto.getContent());

        Long loggedInMemberId = getLoggedInMemberId();
        Member loggedInMember = memberService.findById(loggedInMemberId);
        entity.setMember(loggedInMember);

        w1004Repository.save(entity);

        return "success";
    }

    @GetMapping("/api/events")
    @ResponseBody
    public List<W1004EventDTO> getAllEvents() {
        return w1004service.getAllEvents();
    }

    private Long getLoggedInMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof W1001_CustomDetails customDetails) {
                Member loggedInMember = customDetails.getLoggedInMember();
                if (loggedInMember != null) {
                    return loggedInMember.getId();
                }
            }
        }
        return null;
    }


}

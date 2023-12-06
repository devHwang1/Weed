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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        List<W1004EventDTO> eventDTOs = w1004service.getAllEvents();

        // 현재 로그인된 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            W1001_CustomDetails customDetails = (W1001_CustomDetails) authentication.getPrincipal();
            Member loggedInMember = customDetails.getLoggedInMember();

            // 이벤트 정보에 현재 로그인된 사용자의 정보 추가 또는 이름 변경
            eventDTOs.forEach(eventDTO -> {
                if (loggedInMember != null && loggedInMember.getId().equals(eventDTO.getMemberId())) {
                    eventDTO.setMemberName("나");
                }
            });
        }

        return eventDTOs;
    }

    //이벤트 정보 불러오기
    @GetMapping("/api/event/{eventId}")
    @ResponseBody
    public ResponseEntity<W1004EventDTO> getEventDetails(@PathVariable Long eventId) {
        // eventId를 사용하여 이벤트의 상세 정보를 데이터베이스에서 조회
        W1004EventDTO eventDetails = w1004service.getEventDetails(eventId);

        if (eventDetails != null) {
            return ResponseEntity.ok(eventDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
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

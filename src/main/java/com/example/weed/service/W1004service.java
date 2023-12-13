package com.example.weed.service;

import com.example.weed.dto.W1004EventDTO;
import com.example.weed.entity.Member;
import com.example.weed.entity.Schedule;
import com.example.weed.repository.W1004Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class W1004service {
    @Autowired
    private W1001_MemberService memberService;

    @Autowired
    private W1004Repository w1004Repository;


    @Transactional
    public List<W1004EventDTO> getAllEvents() {
        // 모든 멤버의 스케줄 가져오기
        List<Schedule> allSchedules = w1004Repository.findAll();

        // W1004Entity를 W1004EventDTO로 변환
        return allSchedules.stream()
                .map(this::convertToW1004EventDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveSchedule(W1004EventDTO w1004EventDTO) {
        Member loggedInMember = memberService.getLoggedInMember();

        if (loggedInMember != null) {
            // W1004EventDTO를 W1004Entity로 변환
            Schedule schedule = convertToW1004Entity(w1004EventDTO);

            // 스케줄 엔터티에 멤버 설정
            schedule.setMember(loggedInMember);

            // 스케줄 저장
            w1004Repository.save(schedule);
        } else {
            // 로그인되지 않은 경우에 대한 처리
            throw new IllegalStateException("로그인된 멤버가 없습니다.");
        }
    }

    // W1004Entity를 W1004EventDTO로 변환하는 메소드
    private W1004EventDTO convertToW1004EventDTO(Schedule schedule) {
        W1004EventDTO w1004EventDTO = new W1004EventDTO();
        w1004EventDTO.setId(schedule.getScheduleId().toString());
        w1004EventDTO.setTitle(schedule.getScheduleTitle());
        w1004EventDTO.setStart(schedule.getScheduleStart());
        w1004EventDTO.setEnd(schedule.getScheduleEnd());
        w1004EventDTO.setColor(schedule.getScheduleColor());
        w1004EventDTO.setContent(schedule.getScheduleContent());

        // 필요한 경우 추가적인 필드 설정

        return w1004EventDTO;
    }

    // W1004EventDTO를 W1004Entity로 변환하는 메소드
    private Schedule convertToW1004Entity(W1004EventDTO w1004EventDTO) {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(Long.parseLong(w1004EventDTO.getId()));
        schedule.setScheduleTitle(w1004EventDTO.getTitle());
        schedule.setScheduleStart(w1004EventDTO.getStart());
        schedule.setScheduleEnd(w1004EventDTO.getEnd());
        schedule.setScheduleColor(w1004EventDTO.getColor());
        schedule.setScheduleContent(w1004EventDTO.getContent());

        // 필요한 경우 추가적인 필드 설정

        return schedule;
    }

}

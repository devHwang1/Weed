package com.example.weed.service;

import com.example.weed.dto.W1004EventDTO;
import com.example.weed.entity.Member;
import com.example.weed.entity.W1004Entity;
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
        Member loggedInMember = memberService.getLoggedInMember();

        if (loggedInMember != null) {
            // 로그인된 멤버의 스케줄 가져오기
            List<W1004Entity> userSchedules = w1004Repository.findByMember(loggedInMember);

            // W1004Entity를 W1004EventDTO로 변환
            return userSchedules.stream()
                    .map(this::convertToW1004EventDTO)
                    .collect(Collectors.toList());
        } else {
            // 로그인되지 않은 경우에 대한 처리
            throw new IllegalStateException("로그인된 멤버가 없습니다.");
        }
    }

    @Transactional
    public void saveSchedule(W1004EventDTO w1004EventDTO) {
        Member loggedInMember = memberService.getLoggedInMember();

        if (loggedInMember != null) {
            // W1004EventDTO를 W1004Entity로 변환
            W1004Entity w1004Entity = convertToW1004Entity(w1004EventDTO);

            // 스케줄 엔터티에 멤버 설정
            w1004Entity.setMember(loggedInMember);

            // 스케줄 저장
            w1004Repository.save(w1004Entity);
        } else {
            // 로그인되지 않은 경우에 대한 처리
            throw new IllegalStateException("로그인된 멤버가 없습니다.");
        }
    }

    // W1004Entity를 W1004EventDTO로 변환하는 메소드
    private W1004EventDTO convertToW1004EventDTO(W1004Entity w1004Entity) {
        W1004EventDTO w1004EventDTO = new W1004EventDTO();
        w1004EventDTO.setId(w1004Entity.getScheduleId().toString());
        w1004EventDTO.setTitle(w1004Entity.getScheduleTitle());
        w1004EventDTO.setStart(w1004Entity.getScheduleStart());
        w1004EventDTO.setEnd(w1004Entity.getScheduleEnd());
        w1004EventDTO.setColor(w1004Entity.getScheduleColor());
        w1004EventDTO.setContent(w1004Entity.getScheduleContent());

        // 필요한 경우 추가적인 필드 설정

        return w1004EventDTO;
    }

    // W1004EventDTO를 W1004Entity로 변환하는 메소드
    private W1004Entity convertToW1004Entity(W1004EventDTO w1004EventDTO) {
        W1004Entity w1004Entity = new W1004Entity();
        w1004Entity.setScheduleId(Long.parseLong(w1004EventDTO.getId()));
        w1004Entity.setScheduleTitle(w1004EventDTO.getTitle());
        w1004Entity.setScheduleStart(w1004EventDTO.getStart());
        w1004Entity.setScheduleEnd(w1004EventDTO.getEnd());
        w1004Entity.setScheduleColor(w1004EventDTO.getColor());
        w1004Entity.setScheduleContent(w1004EventDTO.getContent());

        // 필요한 경우 추가적인 필드 설정

        return w1004Entity;
    }

}

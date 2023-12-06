package com.example.weed.repository;

import com.example.weed.entity.Member;
import com.example.weed.entity.W1004Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface W1004Repository extends JpaRepository<W1004Entity, String> {
    // 모든 멤버의 일정 가져오기
    List<W1004Entity> findAll();

    List<W1004Entity> findByMember(Member loggedInMember);
}

package com.example.weed.repository;

import com.example.weed.entity.Member;
import com.example.weed.entity.Working;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface W2002_WorkingRepository extends JpaRepository<Working, Long> {
    Optional<Object> findByMemberAndDate(Member foundMember, LocalDate now);
}
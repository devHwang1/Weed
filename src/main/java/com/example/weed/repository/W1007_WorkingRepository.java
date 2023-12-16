package com.example.weed.repository;

import com.example.weed.dto.W1007_WorkingDTO;
import com.example.weed.entity.Working;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface W1007_WorkingRepository extends JpaRepository<Working, Long> {

}

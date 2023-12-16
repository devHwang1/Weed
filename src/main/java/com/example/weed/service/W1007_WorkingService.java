package com.example.weed.service;

import com.example.weed.dto.W1007_WorkingDTO;
import com.example.weed.entity.Working;
import com.example.weed.repository.W1007_WorkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class W1007_WorkingService {
    private final W1007_WorkingRepository w1007_WorkingRepository;

    @Autowired
    public W1007_WorkingService(W1007_WorkingRepository w1007_WorkingRepository) {
        this.w1007_WorkingRepository = w1007_WorkingRepository;
    }

    public List<Working> getWorkingList() {
        // W1007_WorkingRepository를 통해 DB에서 작업 정보를 가져오는 메서드 호출
        return w1007_WorkingRepository.findAll();
    }
}

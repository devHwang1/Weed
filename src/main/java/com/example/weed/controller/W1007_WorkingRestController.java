package com.example.weed.controller;

import com.example.weed.dto.W1007_workingDTO;
import com.example.weed.entity.Working;
import com.example.weed.service.W1007_WorkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class W1007_WorkingRestController {
    private final W1007_WorkingService W1007_WorkingService;

    @Autowired
    public W1007_WorkingRestController(W1007_WorkingService w1007WorkService) {
        this.W1007_WorkingService = w1007WorkService;
    }

    @GetMapping("/workingData")
    public List<W1007_workingDTO> getWorkingData() {
        // W1007_WorkService를 통해 DB에서 데이터를 가져오기
        return W1007_WorkingService.getAllWorkings().stream()
                .map(Working::toDTO) // 엔터티를 DTO로 변환
                .collect(Collectors.toList());
    }
}

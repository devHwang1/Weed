package com.example.weed.controller;

import com.example.weed.dto.W1007_workingDTO;
import com.example.weed.service.W1007_WorkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.weed.entity.Working;  // 이 부분을 추가

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class W1007_workingController {

    private final W1007_WorkingService W1007_WorkingService;

    @Autowired
    public W1007_workingController(W1007_WorkingService w1007WorkService) {
        this.W1007_WorkingService = w1007WorkService;
    }

    @GetMapping("/working")
    public String Work(Model model) {
        // W1007_WorkService를 통해 DB에서 데이터를 가져오기
        List<W1007_workingDTO> workingList = W1007_WorkingService.getAllWorkings().stream()
                .map(Working::toDTO) // 엔터티를 DTO로 변환
                .collect(Collectors.toList());
        // 모델에 데이터 추가
        model.addAttribute("workingList", workingList);

        return "W1007_working";
    }
}

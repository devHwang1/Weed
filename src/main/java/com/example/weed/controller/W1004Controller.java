package com.example.weed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class W1004Controller {
    @GetMapping("/calendar")
    public String Calendar() {
        return "W1004";
    }

}

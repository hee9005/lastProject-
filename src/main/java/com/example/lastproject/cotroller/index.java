package com.example.lastproject.cotroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Slf4j
public class index {
    @GetMapping("/")
    public String index(){
        return "redirect:/swagger-ui/index.html";
    }
}

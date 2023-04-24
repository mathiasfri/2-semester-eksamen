package com.example.eksamensprojekt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping()
@Controller
public class LoginController {
    @GetMapping("")
    public String landingPage(){
        return "landingPage";
    }
}

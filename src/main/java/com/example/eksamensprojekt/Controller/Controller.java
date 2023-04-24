package com.example.eksamensprojekt.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("")
    public String landingPage(){
        return "landingPage";
    }
}

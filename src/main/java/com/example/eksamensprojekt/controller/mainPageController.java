package com.example.eksamensprojekt.controller;

import com.example.eksamensprojekt.models.User;
import com.example.eksamensprojekt.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("projectCalculator")
@Controller
public class mainPageController {
    private UserRepository userRepository;
    private LoginController loginController;

    public mainPageController(UserRepository userRepository, LoginController loginController) {
        this.userRepository = userRepository;
        this.loginController = loginController;
    }
    @GetMapping("/create")
    public String createUser(Model model){
        User newUser = new User();
        model.addAttribute("newUser", newUser);
        return "create-user";
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute User newUser, Model model){
        int userId = userRepository.createUser(newUser);
        model.addAttribute("email", newUser.getEmail());
        model.addAttribute("password", newUser.getPassword());
        model.addAttribute("userId", userId);
        return "userCreated";
    }

    @GetMapping("/mainpage/{uid}")
    public String mainPage(@PathVariable int uid, Model model, HttpSession session){
        User user = userRepository.getUser(uid);

        model.addAttribute("userId", user.getUserId());
        model.addAttribute("email", user.getEmail());

        return loginController.isLoggedIn(session, uid) ? "mainPage" : "login";
    }
}

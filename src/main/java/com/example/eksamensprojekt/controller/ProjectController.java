package com.example.eksamensprojekt.controller;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.repository.ProjectRepository;
import com.example.eksamensprojekt.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("projectCalculator")
@Controller
public class ProjectController {
    private ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    @GetMapping("/createproject/{uid}")
    public String createWish(@PathVariable int uid, Model model){
        Project newProject = new Project();
        newProject.setUserId(uid);
        model.addAttribute("newProject", newProject);
        return "create-project";
    }
    @PostMapping("/addproject")
    public String addWish(@ModelAttribute Project newProject){
        projectRepository.createProject(newProject);
        return "redirect:/projectCalculator/mainPage/" + newProject.getUserId();
    }
}

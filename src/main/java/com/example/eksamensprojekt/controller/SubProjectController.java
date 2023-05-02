package com.example.eksamensprojekt.controller;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.models.SubProject;
import com.example.eksamensprojekt.repository.ProjectRepository;
import com.example.eksamensprojekt.repository.SubProjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("projectCalculator")
@Controller
public class SubProjectController {
    private SubProjectRepository subProjectRepository;

    public SubProjectController(SubProjectRepository subProjectRepository) {
        this.subProjectRepository = subProjectRepository;
    }
    @GetMapping("/createsubproject/{uid}")
    public String createWish(@PathVariable int uid, Model model){
        SubProject newSubProject = new SubProject();
        newSubProject.setId(uid);
        model.addAttribute("newSubProject", newSubProject);
        return "create-subproject";
    }
    @PostMapping("/addsubproject")
    public String addWish(@ModelAttribute SubProject newSubProject){
        subProjectRepository.createProject(newSubProject);
        return "redirect:/projectCalculator/mainPage/" + newSubProject.getId();
    }
}


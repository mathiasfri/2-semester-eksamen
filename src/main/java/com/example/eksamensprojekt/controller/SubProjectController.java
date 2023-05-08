package com.example.eksamensprojekt.controller;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.models.SubProject;
import com.example.eksamensprojekt.repository.ProjectRepository;
import com.example.eksamensprojekt.repository.SubProjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("projectCalculator")
@Controller
public class SubProjectController {
    private SubProjectRepository subProjectRepository;
    private ProjectRepository projectRepository;

    public SubProjectController(SubProjectRepository subProjectRepository, ProjectRepository projectRepository) {
        this.subProjectRepository = subProjectRepository;
        this.projectRepository = projectRepository;
    }
    @GetMapping("/createsubproject/{pid}")
    public String createSubProject(@PathVariable int pid, Model model){
        SubProject newSubProject = new SubProject();
        newSubProject.setProjectId(pid);
        model.addAttribute("newSubProject", newSubProject);

        return "create-subproject";
    }
    @PostMapping("/addsubproject")
    public String addSubProject(@ModelAttribute SubProject newSubProject){
        subProjectRepository.createProject(newSubProject);
        return "redirect:/projectCalculator/mainPage/" + newSubProject.getProjectId();
    }
    @GetMapping("/updatesubproject/{sid}")
    public String updateSubProject(@PathVariable int sid, Model model){
        SubProject updateSubProject = subProjectRepository.getSpecificSubProject(sid);
        model.addAttribute("updateSubProject", updateSubProject);
        return"update-subproject";
    }

    @PostMapping("/updatesubproject")
    public String updateUserSubProject(@ModelAttribute SubProject subProjectUpdate){
        subProjectRepository.updateSubProject(subProjectUpdate);
        return "redirect:/projectCalculator/mainPage/" + subProjectUpdate.getProjectId();
    }
}



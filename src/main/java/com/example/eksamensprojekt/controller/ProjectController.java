package com.example.eksamensprojekt.controller;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.models.SubProject;
import com.example.eksamensprojekt.repository.ProjectRepository;
import com.example.eksamensprojekt.repository.SubProjectRepository;
import com.example.eksamensprojekt.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("projectCalculator")
@Controller
public class ProjectController {
    private ProjectRepository projectRepository;
    private SubProjectRepository subProjectRepository;

    public ProjectController(ProjectRepository projectRepository, SubProjectRepository subProjectRepository) {
        this.projectRepository = projectRepository;
        this.subProjectRepository = subProjectRepository;
    }
    @GetMapping("/createproject/{uid}")
    public String createProject(@PathVariable int uid, Model model){
        Project newProject = new Project();
        newProject.setUserId(uid);
        model.addAttribute("newProject", newProject);
        return "create-project";
    }
    @PostMapping("/addproject")
    public String addProject(@ModelAttribute Project newProject){
        projectRepository.createProject(newProject);
        return "redirect:/projectCalculator/mainPage/" + newProject.getUserId();
    }

    @GetMapping("/project/{pid}")
    public String projectView(@PathVariable int pid, Model model){
        Project project = projectRepository.getSpecificProject(pid);

        String projectTitle = project.getTitle();
        model.addAttribute("projectTitle", projectTitle);

        model.addAttribute("projectID", pid);

        List<SubProject> subProjects = subProjectRepository.getSubProjects(pid);
        model.addAttribute("subProjects", subProjects);

        return "viewProject";
    }
}

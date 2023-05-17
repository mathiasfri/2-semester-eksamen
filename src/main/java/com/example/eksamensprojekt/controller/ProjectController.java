package com.example.eksamensprojekt.controller;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.models.SubProject;
import com.example.eksamensprojekt.models.Tasks;
import com.example.eksamensprojekt.repository.*;
import org.springframework.scheduling.config.Task;
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
    private ProjectUserRepository projectUserRepository;
    private TasksRepository tasksRepository;
    private SubProjectUserRepository subProjectUserRepository;

    public ProjectController(ProjectRepository projectRepository, SubProjectRepository subProjectRepository,
    ProjectUserRepository projectUserRepository, TasksRepository tasksRepository, SubProjectUserRepository subProjectUserRepository) {
        this.projectRepository = projectRepository;
        this.subProjectRepository = subProjectRepository;
        this.projectUserRepository = projectUserRepository;
        this.tasksRepository = tasksRepository;
        this.subProjectUserRepository = subProjectUserRepository;
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

        String projectDescription = project.getDescription();
        model.addAttribute("projectDescription", projectDescription);

        model.addAttribute("projectID", pid);

        List<SubProject> subProjects = subProjectRepository.getSubProjects(pid);
        model.addAttribute("subProjects", subProjects);

        for (SubProject s : subProjects){
            List<Tasks> tasks = tasksRepository.getTaskList(s.getId());
            s.setTasks(tasks);
        }


        return "viewProject";
    }
    @GetMapping("/viewassignedproject/{pid}")
    public String assignedprojectView(@PathVariable int pid, Model model){
        Project assignedProject = projectRepository.getSpecificAssignedProject(pid);

        String projectTitle = assignedProject.getTitle();
        model.addAttribute("projectTitle", projectTitle);

        String projectDescription = assignedProject.getDescription();
        model.addAttribute("projectDescription", projectDescription);

        model.addAttribute("projectID", pid);
        model.addAttribute("userId", assignedProject.getUserId());

        List<SubProject> subProjects = subProjectRepository.getSubProjects(pid);
        model.addAttribute("subProjects", subProjects);

        for (SubProject s : subProjects){
            List<Tasks> tasks = tasksRepository.getTaskList(s.getId());
            s.setTasks(tasks);
        }


        return "viewAssignedProject";
    }

    @PostMapping("/assignuser/{projectId}")
    public String assignUserToProject(@PathVariable int projectId, @RequestParam("email") String userEmail, Model model) {
        List<Integer> listOfUserIds = new ArrayList<>();
        int userId = projectUserRepository.getUserIdByEmail(userEmail);
        model.addAttribute("projectID", projectId);
        if(userId != -1) {
            listOfUserIds.add(userId);
            projectUserRepository.assignUsersToProject(projectId, listOfUserIds);
        }

        return "redirect:/projectCalculator/mainPage/" + projectId;
    }

    @GetMapping("/updateproject/{pid}")
    public String updateProject(@PathVariable int pid, Model model){
        Project updateProject = projectRepository.getSpecificProject(pid);
        model.addAttribute("updateProject", updateProject);
        return"update-project";
    }

    @PostMapping("/updateproject")
    public String updateUserProject(@ModelAttribute Project projectUpdate){
        projectRepository.updateProject(projectUpdate);
        return "redirect:/projectCalculator/mainPage/" + projectUpdate.getUserId();
    }
}

package com.example.eksamensprojekt.controller;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.models.SubProject;
import com.example.eksamensprojekt.repository.ProjectRepository;
import com.example.eksamensprojekt.repository.ProjectUserRepository;
import com.example.eksamensprojekt.repository.SubProjectRepository;
import com.example.eksamensprojekt.repository.SubProjectUserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("projectCalculator")
@Controller
public class SubProjectController {
    private SubProjectRepository subProjectRepository;
    private ProjectUserRepository projectUserRepository;
    private SubProjectUserRepository subProjectUserRepository;
    private ProjectRepository projectRepository;

    public SubProjectController(SubProjectRepository subProjectRepository, ProjectUserRepository projectUserRepository,
                                SubProjectUserRepository subProjectUserRepository, ProjectRepository projectRepository) {
        this.subProjectRepository = subProjectRepository;
        this.projectUserRepository = projectUserRepository;
        this.subProjectUserRepository = subProjectUserRepository;
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
    public String addSubProject(@ModelAttribute SubProject newSubProject, Model model){
        int projectId = newSubProject.getProjectId();
        Project project = projectRepository.getSpecificProject(projectId);
        // Method that doesn't allow user to type a date that exceeds project deadline.
        LocalDate projectDeadline = project.getDeadline();
        LocalDate subprojectDeadline = newSubProject.getDeadline();
        if(subprojectDeadline.isAfter(projectDeadline)) {
            model.addAttribute("newSubProject", newSubProject);
            model.addAttribute("projectDeadline", projectDeadline);
            model.addAttribute("deadLineError", true);
            return "create-subproject";
        }
        subProjectRepository.createProject(newSubProject);
        double updatedProjectTimeSpent = projectRepository.calculateProjectTimeSpent(projectId);
        project.setTimeSpent(updatedProjectTimeSpent);

        projectRepository.updateProject(project);
        return "redirect:/projectCalculator/mainPage/" + projectId;
    }
    @GetMapping("/updatesubproject/{sid}")
    public String updateSubProject(@PathVariable int sid, Model model){
        SubProject updateSubProject = subProjectRepository.getSpecificSubProject(sid);
        model.addAttribute("updateSubProject", updateSubProject);
        return"update-subproject";
    }

    @PostMapping("/updatesubproject")
    public String updateUserSubProject(@ModelAttribute SubProject updateSubProject, Model model){
        // Method that doesn't allow user to type a date that exceeds project deadline.
        int projectId = updateSubProject.getProjectId();
        Project project = projectRepository.getSpecificProject(projectId);
        LocalDate updatedProjectDeadline = project.getDeadline();
        LocalDate subprojectDeadline = updateSubProject.getDeadline();
        if(subprojectDeadline.isAfter(updatedProjectDeadline)) {
            model.addAttribute("updateSubProject", updateSubProject);
            model.addAttribute("updatedProjectDeadline", updatedProjectDeadline);
            model.addAttribute("updatedDeadLineError", true);
            return "update-subproject";
        }
        subProjectRepository.updateSubProject(updateSubProject);

        double updatedProjectTimeSpent = projectRepository.calculateProjectTimeSpent(projectId);
        project.setTimeSpent(updatedProjectTimeSpent);
        projectRepository.updateProject(project);
        return "redirect:/projectCalculator/mainPage/" + projectId;
    }
    @PostMapping("/assignusertosub/{subId}")
    public String assignUserToSubProject(@PathVariable int subId, @RequestParam("email") String userEmail) {
        List<Integer> listOfUserIds = new ArrayList<>();
        int userId = projectUserRepository.getUserIdByEmail(userEmail);
        if(userId != -1) {
            listOfUserIds.add(userId);
            subProjectUserRepository.assignUsersToSubProject(subId, listOfUserIds);
        }

        return "redirect:/projectCalculator/mainPage/" + subId;
    }
    @DeleteMapping("/deletesubproject/{pid}")
    public String deleteProject(@PathVariable int pid) {
        SubProject subProjectDelete = subProjectRepository.getSpecificSubProject(pid);
        int projectId = subProjectDelete.getProjectId();
        subProjectRepository.deleteSubProject(pid);

        Project project = projectRepository.getSpecificProject(projectId);
        double updatedProjectTimeSpent = projectRepository.calculateProjectTimeSpent(projectId);
        project.setTimeSpent(updatedProjectTimeSpent);
        projectRepository.updateProject(project);

        return "redirect:/projectCalculator/mainPage/" + projectId;
    }
}



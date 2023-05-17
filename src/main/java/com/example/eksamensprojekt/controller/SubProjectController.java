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

import java.util.ArrayList;
import java.util.List;

@RequestMapping("projectCalculator")
@Controller
public class SubProjectController {
    private SubProjectRepository subProjectRepository;
    private ProjectUserRepository projectUserRepository;
    private SubProjectUserRepository subProjectUserRepository;

    public SubProjectController(SubProjectRepository subProjectRepository, ProjectUserRepository projectUserRepository,
                                SubProjectUserRepository subProjectUserRepository) {
        this.subProjectRepository = subProjectRepository;
        this.projectUserRepository = projectUserRepository;
        this.subProjectUserRepository = subProjectUserRepository;
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
    public String deleteProject(@PathVariable int pid, @ModelAttribute Project projectDelete) {
        subProjectRepository.deleteSubProject(pid);
        return "redirect:/projectCalculator/mainPage/" + projectDelete.getUserId();
    }
}



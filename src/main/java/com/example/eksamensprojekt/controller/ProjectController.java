package com.example.eksamensprojekt.controller;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.models.SubProject;
import com.example.eksamensprojekt.models.Tasks;
import com.example.eksamensprojekt.models.User;
import com.example.eksamensprojekt.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("projectCalculator")
@Controller
public class ProjectController {
    private ProjectRepository projectRepository;
    private SubProjectRepository subProjectRepository;
    private ProjectUserRepository projectUserRepository;
    private TasksRepository tasksRepository;
    private TasksUserRepository tasksUserRepository;
    private SubProjectUserRepository subProjectUserRepository;
    private UserRepository userRepository;

    public ProjectController(ProjectRepository projectRepository, SubProjectRepository subProjectRepository,
    ProjectUserRepository projectUserRepository, TasksRepository tasksRepository, SubProjectUserRepository subProjectUserRepository
    , UserRepository userRepository, TasksUserRepository tasksUserRepository) {
        this.projectRepository = projectRepository;
        this.subProjectRepository = subProjectRepository;
        this.projectUserRepository = projectUserRepository;
        this.tasksRepository = tasksRepository;
        this.subProjectUserRepository = subProjectUserRepository;
        this.userRepository = userRepository;
        this.tasksUserRepository = tasksUserRepository;
    }
    @GetMapping("/createproject/{uid}")
    public String createProject(@PathVariable int uid, Model model){
        Project newProject = new Project();
        newProject.setUserId(uid);
        User user = userRepository.getUser(uid);
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("newProject", newProject);
        return "create-project";
    }
    @PostMapping("/addproject")
    public String addProject(@ModelAttribute Project newProject){
        projectRepository.createProject(newProject);
        return "redirect:/projectCalculator/mainPage/" + newProject.getUserId();
    }

    @GetMapping("/project/{pid}")
    public String projectView(@PathVariable int pid, @RequestParam("userId") int userId, Model model) {
        User loggedInUser = userRepository.getUser(userId);

        Project project = projectRepository.getSpecificProject(pid);

        List<Integer> listOfAssignedUsersToProject = projectUserRepository.checkIfAssignedToProject(pid);
        for (Integer i : listOfAssignedUsersToProject){
            if (i.equals(loggedInUser.getUserId())){
                return "viewProject";
            }
            else{
                return "noaccess";
            }
        }

        // Check if the logged-in user has access to the project
        if (project.getUserId() == loggedInUser.getUserId()) {
            // Handle unauthorized access, e.g., redirect to an error page
            return "viewProject";
        }

        model.addAttribute("userId", loggedInUser.getUserId());
        String projectTitle = project.getTitle();
        model.addAttribute("projectTitle", projectTitle);

        String projectDescription = project.getDescription();
        model.addAttribute("projectDescription", projectDescription);

        model.addAttribute("projectID", pid);

        List<SubProject> subProjects = subProjectRepository.getSubProjects(pid);
        model.addAttribute("subProjects", subProjects);

        Map<Integer, Boolean> assignedSubStatusMap = new HashMap<>();
        List<Tasks> tasks = new ArrayList<>();

        for (SubProject s : subProjects) {
            tasks = tasksRepository.getTaskList(s.getId());
            s.setTasks(tasks);

            boolean isAssignedToSub = subProjectUserRepository.isAssignedToSubproject(loggedInUser.getUserId(), s.getId());
            assignedSubStatusMap.put(s.getId(), isAssignedToSub);
        }
        model.addAttribute("assignedSubStatusMap", assignedSubStatusMap);


        Map<Integer, Boolean> assignedTaskStatusMap = new HashMap<>();
        for (Tasks t : tasks){
            boolean isAssignedToTask = tasksUserRepository.isAssignedToTask(loggedInUser.getUserId(), t.getId());
            assignedTaskStatusMap.put(t.getId(), isAssignedToTask);
        }
        model.addAttribute("assignedTaskStatusMap", assignedTaskStatusMap);


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
        User user = userRepository.getUser(updateProject.getUserId());
        model.addAttribute("userId", user.getUserId());
        return"update-project";
    }

    @PostMapping("/updateproject")
    public String updateUserProject(@ModelAttribute Project projectUpdate){
        projectRepository.updateProject(projectUpdate);
        return "redirect:/projectCalculator/mainPage/" + projectUpdate.getUserId();
    }
    @DeleteMapping("/deleteproject/{pid}")
    public String deleteProject(@PathVariable int pid, @ModelAttribute Project projectDelete) {
        projectRepository.deleteProject(pid);
        return "redirect:/projectCalculator/mainPage/" + projectDelete.getUserId();
    }
}

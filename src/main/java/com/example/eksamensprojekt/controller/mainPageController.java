package com.example.eksamensprojekt.controller;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.models.SubProject;
import com.example.eksamensprojekt.models.Tasks;
import com.example.eksamensprojekt.models.User;
import com.example.eksamensprojekt.repository.*;
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
    private ProjectRepository projectRepository;
    private SubProjectRepository subProjectRepository;
    private TasksRepository tasksRepository;
    private TasksUserRepository tasksUserRepository;
    public mainPageController(UserRepository userRepository, LoginController loginController, ProjectRepository projectRepository,
                              SubProjectRepository subProjectRepository, TasksRepository tasksRepository,
                              TasksUserRepository tasksUserRepository) {
        this.userRepository = userRepository;
        this.loginController = loginController;
        this.projectRepository = projectRepository;
        this.subProjectRepository = subProjectRepository;
        this.tasksRepository = tasksRepository;
        this.tasksUserRepository = tasksUserRepository;
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

    @GetMapping("/mainPage/{uid}")
    public String mainPage(@PathVariable int uid, Model model, HttpSession session){
        User user = userRepository.getUser(uid);
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("email", user.getEmail());

        List<Project> projectList = projectRepository.getProjectList((uid));
        model.addAttribute("projectList", projectList);

        List<SubProject> subProjectList = subProjectRepository.getSubProjects(uid);
        model.addAttribute("subProjectList", subProjectList);

        List<Tasks> tasksList = tasksRepository.getTaskList(uid);
        model.addAttribute("tasksList", tasksList);
        return loginController.isLoggedIn(session, uid) ? "mainPage" : "login";
    }
    @GetMapping("/assignedprojects/{uid}")
    public String getAssignedProjects(@PathVariable int uid, Model model) {
        List<Project> assignedProjects = projectRepository.getAssignedProjects(uid);
        model.addAttribute("assignedProjects", assignedProjects);
        return "assignedProjects";
    }
    @GetMapping("/assignedsubprojects/{spid}")
    public String getAssignedSubProjects(@PathVariable int spid, Model model) {
        Project assignedProject = projectRepository.getSpecificProject(spid);
        List<SubProject> assignedSubProjects = subProjectRepository.getAssignedSubProjects(spid);
        model.addAttribute("assignedSubProjects", assignedSubProjects);
        model.addAttribute("specificProject", assignedProject);

        return "assignedSubProjects";
    }
    @GetMapping("/assignedtasks/{tid}")
    public String getAssignedTasks(@PathVariable int tid, Model model) {
        List<Tasks> assignedTasks = tasksRepository.getAssignedTasks(tid);
        model.addAttribute("pid", projectRepository.getSpecificProject(tid));
        model.addAttribute("assignedTasks", assignedTasks);

        return "assignedTasks";
    }


}

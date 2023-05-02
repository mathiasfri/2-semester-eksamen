package com.example.eksamensprojekt.controller;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.models.SubProject;
import com.example.eksamensprojekt.models.Tasks;
import com.example.eksamensprojekt.models.User;
import com.example.eksamensprojekt.repository.ProjectRepository;
import com.example.eksamensprojekt.repository.SubProjectRepository;
import com.example.eksamensprojekt.repository.TasksRepository;
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
    private ProjectRepository projectRepository;
    private SubProjectRepository subProjectRepository;
    private TasksRepository tasksRepository;

    public mainPageController(UserRepository userRepository, LoginController loginController, ProjectRepository projectRepository,
                              SubProjectRepository subProjectRepository, TasksRepository tasksRepository) {
        this.userRepository = userRepository;
        this.loginController = loginController;
        this.projectRepository = projectRepository;
        this.subProjectRepository = subProjectRepository;
        this.tasksRepository = tasksRepository;
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
}

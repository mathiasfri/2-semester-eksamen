package com.example.eksamensprojekt.controller;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.models.SubProject;
import com.example.eksamensprojekt.models.Tasks;
import com.example.eksamensprojekt.models.User;
import com.example.eksamensprojekt.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("projectCalculator")
@Controller
public class TaskController {
    private TasksRepository tasksRepository;
    private ProjectUserRepository projectUserRepository;
    private TasksUserRepository tasksUserRepository;
    private SubProjectRepository subProjectRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;

    public TaskController(TasksRepository tasksRepository, ProjectUserRepository projectUserRepository, TasksUserRepository
            tasksUserRepository, SubProjectRepository subProjectRepository, ProjectRepository projectRepository,
                          UserRepository userRepository) {
        this.tasksRepository = tasksRepository;
        this.projectUserRepository = projectUserRepository;
        this.tasksUserRepository = tasksUserRepository;
        this.subProjectRepository = subProjectRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }
    @GetMapping("/createtask/{sid}")
    public String createTask(@PathVariable int sid, Model model){
        Tasks newTask = new Tasks();
        newTask.setSubId(sid);
       SubProject subProject = subProjectRepository.getSpecificSubProject(sid);
       Project project = projectRepository.getSpecificProject(subProject.getProjectId());
       User user = userRepository.getUser(project.getUserId());
       model.addAttribute("userId", user.getUserId());
        model.addAttribute("newTask", newTask);
        return "create-task";
    }
    @PostMapping("/addtask")
    public String addTask(@ModelAttribute Tasks newTask, Model model) {
        int subId = newTask.getSubId();
        SubProject subProject = subProjectRepository.getSpecificSubProject(subId);
        LocalDate subProjectDeadline = subProject.getDeadline();
        LocalDate taskDeadline = newTask.getDeadline();
        if (taskDeadline.isAfter(subProjectDeadline)) {
            model.addAttribute("newTask", newTask);
            model.addAttribute("subProjectDeadLine", subProjectDeadline);
            model.addAttribute("deadlineError", true);
            return "create-task";
        }


        tasksRepository.createTask(newTask);

        // Update the time spent for the subProject and project
        double subProjectTimeSpent = subProjectRepository.calculateSubProjectTimeSpent(subId);
        subProject.setTimeSpent(subProjectTimeSpent);
        subProjectRepository.updateSubProject(subProject);

        int projectId = subProject.getProjectId();
        double projectTimeSpent = projectRepository.calculateProjectTimeSpent(projectId);
        Project project = projectRepository.getSpecificProject(projectId);
        project.setTimeSpent(projectTimeSpent);
        projectRepository.updateProject(project);


        return "redirect:/projectCalculator/mainPage/" + subId;
    }

    @GetMapping("/updatetask/{tid}")
    public String updateTask(@PathVariable int tid, Model model){
        Tasks updateTask = tasksRepository.getSpecificTask(tid);
        SubProject subProject = subProjectRepository.getSpecificSubProject(updateTask.getSubId());
        Project project = projectRepository.getSpecificProject(subProject.getProjectId());
        User user = userRepository.getUser(project.getUserId());
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("updateTask", updateTask);
        return"update-task";
    }

    @PostMapping("/updatetask")
    public String updateUserTask(@ModelAttribute Tasks updateTask, Model model){
        int subId = updateTask.getSubId();
        SubProject subProject = subProjectRepository.getSpecificSubProject(subId);
        LocalDate subProjectDeadline = subProject.getDeadline();
        LocalDate taskDeadline = updateTask.getDeadline();
        if (taskDeadline.isAfter(subProjectDeadline)) {
            model.addAttribute("updateTask", updateTask);
            model.addAttribute("subProjectDeadLine", subProjectDeadline);
            model.addAttribute("updatedDeadLineError", true);
            return "update-task";
        }

        tasksRepository.updateTask(updateTask);

        // Update the time spent for the subProject and project
        double subProjectTimeSpent = subProjectRepository.calculateSubProjectTimeSpent(subId);
        subProject.setTimeSpent(subProjectTimeSpent);
        subProjectRepository.updateSubProject(subProject);

        int projectId = subProject.getProjectId();
        double projectTimeSpent = projectRepository.calculateProjectTimeSpent(projectId);
        Project project = projectRepository.getSpecificProject(projectId);
        project.setTimeSpent(projectTimeSpent);
        projectRepository.updateProject(project);

        return "redirect:/projectCalculator/mainPage/" + updateTask.getId();
    }
    @PostMapping("/assignusertotask/{taskId}")
    public String assignUserToSubProject(@PathVariable int taskId, @RequestParam("email") String userEmail) {
        List<Integer> listOfUserIds = new ArrayList<>();
        int userId = projectUserRepository.getUserIdByEmail(userEmail);
        if(userId != -1) {
            listOfUserIds.add(userId);
            tasksUserRepository.assignUsersToTask(taskId, listOfUserIds);
        }

        return "redirect:/projectCalculator/mainPage/" + taskId;
    }
    @PostMapping("/deletetask/{tid}")
    public String deleteTask(@PathVariable int tid, @ModelAttribute Tasks taskDelete) {
        Tasks deletedTask = tasksRepository.getSpecificTask(tid);
        int subId = deletedTask.getSubId();
        int projectId;

        tasksRepository.deleteTask(tid);

        SubProject subProject = subProjectRepository.getSpecificSubProject(subId);
        projectId = subProject.getProjectId();

        // Update the time spent for the project
        double updatedProjectTimeSpent = projectRepository.calculateProjectTimeSpent(projectId);
        Project project = projectRepository.getSpecificProject(projectId);
        project.setTimeSpent(updatedProjectTimeSpent);
        int userId = project.getUserId();
        projectRepository.updateProject(project);

        return "redirect:/projectCalculator/mainPage/" + userId;
    }

}
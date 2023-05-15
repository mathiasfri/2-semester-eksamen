package com.example.eksamensprojekt.controller;

import com.example.eksamensprojekt.models.SubProject;
import com.example.eksamensprojekt.models.Tasks;
import com.example.eksamensprojekt.repository.ProjectUserRepository;
import com.example.eksamensprojekt.repository.SubProjectRepository;
import com.example.eksamensprojekt.repository.TasksRepository;
import com.example.eksamensprojekt.repository.TasksUserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("projectCalculator")
@Controller
public class TaskController {
    private TasksRepository tasksRepository;
    private ProjectUserRepository projectUserRepository;
    private TasksUserRepository tasksUserRepository;

    public TaskController(TasksRepository tasksRepository, ProjectUserRepository projectUserRepository, TasksUserRepository
                          tasksUserRepository) {
        this.tasksRepository = tasksRepository;
        this.projectUserRepository = projectUserRepository;
        this.tasksUserRepository = tasksUserRepository;
    }
    @GetMapping("/createtask/{sid}")
    public String createTask(@PathVariable int sid, Model model){
        Tasks newTask = new Tasks();
        newTask.setSubId(sid);

        model.addAttribute("newTask", newTask);
        return "create-task";
    }
    @PostMapping("/addtask")
    public String addTask(@ModelAttribute Tasks newTask){
        tasksRepository.createTask(newTask);
        return "redirect:/projectCalculator/mainPage/" + newTask.getSubId();
    }
    @GetMapping("/updatetask/{tid}")
    public String updateTask(@PathVariable int tid, Model model){
        Tasks updateTask = tasksRepository.getSpecificTask(tid);
        model.addAttribute("updateTask", updateTask);
        return"update-Task";
    }

    @PostMapping("/updatetask")
    public String updateUserTask(@ModelAttribute Tasks taskUpdate){
        tasksRepository.updateTask(taskUpdate);
        return "redirect:/projectCalculator/mainPage/" + taskUpdate.getId();
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
}

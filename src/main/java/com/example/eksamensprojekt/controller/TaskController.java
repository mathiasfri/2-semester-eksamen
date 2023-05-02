package com.example.eksamensprojekt.controller;

import com.example.eksamensprojekt.models.SubProject;
import com.example.eksamensprojekt.models.Tasks;
import com.example.eksamensprojekt.repository.SubProjectRepository;
import com.example.eksamensprojekt.repository.TasksRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("projectCalculator")
@Controller
public class TaskController {
    private TasksRepository tasksRepository;

    public TaskController(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }
    @GetMapping("/createtask/{uid}")
    public String createTask(@PathVariable int uid, Model model){
        Tasks newTask = new Tasks();
        newTask.setSubId(uid);
        model.addAttribute("newTask", newTask);
        return "create-task";
    }
    @PostMapping("/addtask")
    public String addTask(@ModelAttribute Tasks newTask){
        tasksRepository.createTask(newTask);
        return "redirect:/projectCalculator/mainPage/" + newTask.getSubId();
    }
}

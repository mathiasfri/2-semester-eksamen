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
}

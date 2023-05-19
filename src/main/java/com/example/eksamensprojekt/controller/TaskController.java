package com.example.eksamensprojekt.controller;

import com.example.eksamensprojekt.models.Project;
import com.example.eksamensprojekt.models.SubProject;
import com.example.eksamensprojekt.models.Tasks;
import com.example.eksamensprojekt.repository.ProjectUserRepository;
import com.example.eksamensprojekt.repository.SubProjectRepository;
import com.example.eksamensprojekt.repository.TasksRepository;
import com.example.eksamensprojekt.repository.TasksUserRepository;
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

    public TaskController(TasksRepository tasksRepository, ProjectUserRepository projectUserRepository, TasksUserRepository
                          tasksUserRepository, SubProjectRepository subProjectRepository) {
        this.tasksRepository = tasksRepository;
        this.projectUserRepository = projectUserRepository;
        this.tasksUserRepository = tasksUserRepository;
        this.subProjectRepository = subProjectRepository;
    }
    @GetMapping("/createtask/{sid}")
    public String createTask(@PathVariable int sid, Model model){
        Tasks newTask = new Tasks();
        newTask.setSubId(sid);

        model.addAttribute("newTask", newTask);
        return "create-task";
    }
    @PostMapping("/addtask")
    public String addTask(@ModelAttribute Tasks newTask, Model model){
        int subId = newTask.getSubId();
        SubProject subProject = subProjectRepository.getSpecificSubProject(subId);
        LocalDate subProjectDeadline = subProject.getDeadline();
        LocalDate taskDeadline = newTask.getDeadline();
        if(taskDeadline.isAfter(subProjectDeadline)) {
            model.addAttribute("newTask", newTask);
            model.addAttribute("subProjectDeadLine", subProjectDeadline);
            model.addAttribute("deadlineError", true);
            return "create-task";
        }
        tasksRepository.createTask(newTask);
        return "redirect:/projectCalculator/mainPage/" + subId;
    }
    @GetMapping("/updatetask/{tid}")
    public String updateTask(@PathVariable int tid, Model model){
            Tasks updateTask = tasksRepository.getSpecificTask(tid);
            model.addAttribute("updateTask", updateTask);
            return"update-Task";
    }

    @PostMapping("/updatetask")
    public String updateUserTask(@ModelAttribute Tasks updateTask, Model model){

        int subId = updateTask.getSubId();
        SubProject subProject = subProjectRepository.getSpecificSubProject(subId);
        LocalDate subProjectDeadline = subProject.getDeadline();
        LocalDate taskDeadline = updateTask.getDeadline();
        if(taskDeadline.isAfter(subProjectDeadline)) {
            model.addAttribute("updateTask", updateTask);
            model.addAttribute("subProjectDeadLine", subProjectDeadline);
            model.addAttribute("updatedDeadLineError", true);
            return "update-task";
        }
        tasksRepository.updateTask(updateTask);
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
    @DeleteMapping("/deletetask/{tid}")
    public String deleteProject(@PathVariable int tid, @ModelAttribute Tasks taskDelete) {
        tasksRepository.deleteTask(tid);
        return "redirect:/projectCalculator/mainPage/" + taskDelete.getId();
    }
}

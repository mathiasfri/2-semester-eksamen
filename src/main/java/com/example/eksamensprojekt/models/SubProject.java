package com.example.eksamensprojekt.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subproject")
public class SubProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private LocalDate deadline;
    private double timeSpent;
    private int projectId;
    @OneToMany
    private List<Tasks> tasks = new ArrayList<>();


    public SubProject() {

    }
    public SubProject(int id, String title, LocalDate deadline,double timeSpent, int projectId) {
        this.title = title;
        this.deadline = deadline;
        this.id = id;
        this.projectId = projectId;
        this.timeSpent = timeSpent;
    }

    public double getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(double timeSpent) {
        this.timeSpent = timeSpent;
    }

    public void setTitle(String title) {
        this.title = title;
    }

   public int getProjectId() {
        return projectId;
   }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }


    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Tasks> getTasks() {
        return tasks;
    }

    public void setTasks(List<Tasks> tasks) {
        this.tasks = tasks;
    }
}

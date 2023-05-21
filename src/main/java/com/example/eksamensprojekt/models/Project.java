package com.example.eksamensprojekt.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate deadline;
    private int budget;
    private String title;
    private int userId;
    private String description;
    private double timeSpent;

    public Project() {

    }
    public Project(int id, String title, LocalDate deadline, int budget, int userId, String description, double timeSpent) {
        this.title = title;
        this.deadline = deadline;
        this.budget = budget;
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.timeSpent = timeSpent;
    }

    public double getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(double timeSpent) {
        this.timeSpent = timeSpent;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

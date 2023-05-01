package com.example.eksamensprojekt.models;

import java.time.LocalDate;

public class Project {
    private LocalDate deadline;
    private int budget;
    private String title;
    private int id;
    private int userId;

    public Project() {

    }
    public Project(int id, String title, LocalDate deadline, int budget, int userId) {
        this.title = title;
        this.deadline = deadline;
        this.budget = budget;
        this.id = id;
        this.userId = userId;
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
}

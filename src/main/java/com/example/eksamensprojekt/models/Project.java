package com.example.eksamensprojekt.models;

import java.time.LocalDate;

public class Project {
    private LocalDate deadline;
    private int budget;
    private String titel;

    public Project(String titel, LocalDate deadline, int budget) {
        this.titel = titel;
        this.deadline = deadline;
        this.budget = budget;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDate(LocalDate deadline) {
        this.deadline = deadline;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }


}

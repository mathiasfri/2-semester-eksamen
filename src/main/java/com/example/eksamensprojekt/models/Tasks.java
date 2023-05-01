package com.example.eksamensprojekt.models;

import java.time.LocalDate;

public class Tasks {
   private int hoursToComplete;
    private String titel;
    private LocalDate deadline;
    private int id;

    public Tasks(int id, String titel, int hoursToComplete, LocalDate deadline) {
        this.titel = titel;
        this.hoursToComplete = hoursToComplete;
        this.deadline = deadline;
        this.id = id;
    }

    public int getHoursToComplete() {
        return hoursToComplete;
    }

    public void setHoursToComplete(int hoursToComplete) {
        this.hoursToComplete = hoursToComplete;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
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
}

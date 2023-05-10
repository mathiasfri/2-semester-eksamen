package com.example.eksamensprojekt.models;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name = "tasks")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double timeSpent;
    private String title;
    private LocalDate deadline;
    private int subId;

    public Tasks() {

    }
    public Tasks(int id, String title, LocalDate deadline, double timeSpent, int subId) {
        this.title = title;
        this.timeSpent = timeSpent;
        this.deadline = deadline;
        this.id = id;
        this.subId = subId;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }

    public double getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(double timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

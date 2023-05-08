package com.example.eksamensprojekt.models;

import java.time.LocalDate;

public class SubProject {
    private String title;
    private LocalDate deadline;
    private int id;
    private int projectId;


    public SubProject() {

    }
    public SubProject(int id, String title, LocalDate deadline, int projectId) {
        this.title = title;
        this.deadline = deadline;
        this.id = id;
        this.projectId = projectId;
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
}

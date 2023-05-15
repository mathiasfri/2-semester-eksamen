package com.example.eksamensprojekt.models;

import jakarta.persistence.*;

@Entity
@Table(name = "project_user")
public class ProjectUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private int projectId;

    public ProjectUser() {

    }

    public ProjectUser(int userId, int projectId) {
        this.userId = userId;
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}

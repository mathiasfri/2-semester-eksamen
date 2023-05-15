package com.example.eksamensprojekt.models;

public class TasksUser {
    private int userId;
    private int taskId;

    public TasksUser() {

    }
    public TasksUser(int userId, int taskId) {
        this.userId = userId;
        this.taskId = taskId;

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}

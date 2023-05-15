package com.example.eksamensprojekt.models;

public class SubProjectUser {
    private int userId;
    private int subId;

    public SubProjectUser() {

    }
    public SubProjectUser(int userId, int subId) {
        this.userId = userId;
        this.subId = subId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }
}

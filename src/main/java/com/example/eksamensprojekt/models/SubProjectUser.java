package com.example.eksamensprojekt.models;

import jakarta.persistence.*;

@Entity
@Table(name = "subproject_user")
public class SubProjectUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

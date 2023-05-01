package com.example.eksamensprojekt.models;

import java.time.LocalDate;

public class SubProject {
    private String titel;
    private LocalDate deadline;

    public SubProject(String titel, LocalDate deadline) {
        this.titel = titel;
        this.deadline = deadline;
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
}

package com.example.myapplication.Model;


import java.sql.Timestamp;

public class Task {
    String content;
    boolean done;
    Timestamp deadline;

    public Task(String content, Timestamp deadline, boolean done) {
        this.content = content;
        this.done = done;
        this.deadline = deadline;
    }

    public Task(String content, Timestamp deadline) {
        this.content = content;
        this.deadline = deadline;
        this.done = false;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }
}

package com.example.myapplication.Model;


import static com.example.myapplication.Utils.Database.DEADLINE_FORMAT;
import static com.example.myapplication.Utils.Helpers.dateFormat;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
    private int id;
    private String content;
    private boolean done;
    private Date deadline;

    public Task(String content, String strDeadline, boolean done) {
        this.content = content;
        try {
            this.deadline = dateFormat.parse(strDeadline);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.done = done;
    }

    public Task(int id, String content, String strDeadline, int done) {
        this.id = id;
        this.content = content;
        try {
            this.deadline = dateFormat.parse(strDeadline);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.done = done == 1;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getDeadline() {
        return deadline;
    }

    public String getStrDeadline() {
        return dateFormat.format(deadline);
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setDeadline(String strDeadline) {
        try {
            this.deadline = dateFormat.parse(strDeadline);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getFullTask() {
        return String.valueOf(id) + "," + content + "," + deadline.toString() + "," + String.valueOf(done);
    }
}

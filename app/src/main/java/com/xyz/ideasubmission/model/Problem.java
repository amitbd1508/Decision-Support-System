package com.xyz.ideasubmission.model;


public class Problem {


    private int id;
    private int likes;
    private String body;
    private String title;
    private String category;
    private String submittedby;
    private String date;

    public Problem() {
    }

    public Problem(String title, String body, String category, String submittedby) {
        this.body = body;
        this.title = title;
        this.category = category;
        this.submittedby = submittedby;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubmittedby() {
        return submittedby;
    }

    public void setSubmittedby(String submittedby) {
        this.submittedby = submittedby;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

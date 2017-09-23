package com.xyz.ideasubmission.model;



public class User {
    public int id;

    public User() {
    }

    public User(int id, String name, String type) {

        this.id = id;
        this.name = name;
        this.type = type;

    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String name,type,email;
}

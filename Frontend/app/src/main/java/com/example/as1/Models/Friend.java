package com.example.as1.Models;

import java.util.List;

public class Friend {
    private Integer Id;
    private String username;
    private String email;
    private List<Course> courseList;

    public Friend(String username, Integer id, String email, List<Course> courseList) {
        this.username = username;
        this.email = email;
        this.Id = id;
        this.courseList = courseList;
    }

    public Friend(String username, Integer id, String email) {
        this.username = username;
        this.email = email;
        this.Id = id;
    }
    public Friend(){}

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
}

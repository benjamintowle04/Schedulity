package onetomany.Advisor;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.transaction.Transactional;
import javax.persistence.OneToOne;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import onetomany.Courses.Course;
import onetomany.Parents.Parent;
import onetomany.StudyTime.StudyTime;

import onetomany.Activity.Activity;

import onetomany.SleepTime.SleepTime;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonIgnore;
import onetomany.Users.User;

@Entity
public class Advisor {

    @ApiModelProperty(notes = "advisor id", example = "1", required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty(notes = "Online?", example = "false", required = false)
    private boolean loggedIn;

    @ApiModelProperty(notes = "Advisor's email", example = "exEmail@Example.ex", required = true)
    private String email;

    @ApiModelProperty(notes = "Advisor's Username", example = "exUsername", required = true)
    private String username;

    @ApiModelProperty(notes = "Advisor's Password", example = "password", required = true)
    private String password;

    @ApiModelProperty(notes = "Type of User", example = "1", required = false)
    private int type = 3;

    @ApiModelProperty(notes = "List of Students names", example = "studnet1, student2, ect.",required = false)
    private ArrayList<String> students = new ArrayList<>();

    @ApiModelProperty(notes = "List of students", required = false)
    @OneToMany
    @JoinColumn(name = "users_id")
    private List<User> student = new ArrayList<>();

    public Advisor(String username, String password, int type, String email)
    {
        this.username = username;
        this.password = password;
        this.type = type;
        this.email = email;
    }

    public Advisor()
    {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<String> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<String> students) {
        this.students = students;
    }

    public List<User> getStudent() {
        return student;
    }

    public void setStudent(List<User> student) {
        this.student = student;
    }
}

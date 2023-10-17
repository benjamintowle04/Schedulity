package onetomany.StudyTime;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import onetomany.Courses.Course;
import onetomany.Users.User;

import java.util.ArrayList;
import java.util.List;

@Entity
public class StudyTime {

    @ApiModelProperty(notes = "Study Time Id", example = "1", required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //id of the class it is referencing
    @ApiModelProperty(notes = "Id of the Given Course", example = "1", required = true)
    private int courseId;

    //name of course
    @ApiModelProperty(notes = "The Name of the Course", example = "Course 101", required = true)
    private String courseName;

    //level of importance the user finds this class
    @ApiModelProperty(notes = "How Important the User Finds this Course", example = "4", required = false)
    private int importance;

    //time in hours for this course
    @ApiModelProperty(notes = "The Amount of Time the User Wants to Spend Studying for this Course in a Week", example = "6.0", required = false)
    private double time;

    //the number of credits the given course is
    @ApiModelProperty(notes = "How Many Credits the Given Course is", example = "3", required = true)
    private int credits;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore

    private User user;

    public StudyTime(){

    }

    public StudyTime(int courseId, String courseName, int importance, double time, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.importance = importance;
        this.time = time;
        this.credits = credits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCredits(){ return credits;}

    public void setCredits(int credits){ this.credits = credits;}
}

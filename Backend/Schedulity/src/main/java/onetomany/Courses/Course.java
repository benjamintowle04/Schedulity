package onetomany.Courses;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;


import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import onetomany.Users.User;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Vivek Bengre
 */ 

@Entity
public class Course {

    /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */

    @ApiModelProperty(notes="Course id", example = "1", required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //time that class starts
    @ApiModelProperty(notes = "Course Start Time", example = "9.0", required = true)
    private double startTime;

    //time that class ends
    @ApiModelProperty(notes = "Course End Time", example = "10.0", required = true)
    private double endTime;

    //date course starts
    private String startDate;

    //date course ends
    private String endDate;

    //notes
    private String notes;

    //the course name
    @ApiModelProperty(notes = "Course Name", example = "Course 101", required = true)
    private String course;

    //number of credits and course is
    @ApiModelProperty(notes = "Amount of credits in course", example = "3", required = true)
    private int credits;

    //location of the class
    @ApiModelProperty(notes = "Location of the course", example = "Hall", required = true)
    private String location;

    //days that you have the course
    @ApiModelProperty(notes = "Days of the week", example = "M W F", required = true)
    private String days;


    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/laptop objects (laptop->user->laptop->...)
     */
    @ManyToMany
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    public Course( String course, String location, String days, double startTime, double endTime, int credits, String startDate, String endDate, String notes) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.course = course;
        this.credits = credits;
        this.location = location;
        this.days = days;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
    }

    public Course() {
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public double getStartTime(){
        return startTime;
    }

    public void setStartTime(double startTime){
        this.startTime = startTime;
    }

    public double getEndTime(){
        return endTime;
    }

    public void setEndTime(double endTime){
        this.endTime = endTime;
    }

    public String getCourse(){
        return course;
    }

    public void setCourse(String course){
        this.course = course;
    }

    public int getCredits(){
        return credits;
    }

    public void setCredits(int credits){
        this.credits = credits;
    }

    public List<User> getUsers(){
        return users;
    }

    public void setUsers(List<User> users){
        this.users = users;
    }

    public String getLocation(){return location;}

    public void setLocation(String location){this.location = location;}

    public String getDays(){return days;}

    public void setDays(String days){this.days = days;}

    public String getStartDate(){return startDate;}

    public void setStartDate(String startDate){
        this.startDate = startDate;
    }

    public String getEndDate(){return endDate;}

    public void setEndDate(String endDate){;
        this.endDate = endDate;
    }

    public String getNotes(){return notes;}

    public void setNotes(String notes){this.notes = notes;}

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"startTime\":" + startTime +
                ", \"endTime\":" + endTime +
                ", \"course\":\"" + course + '\"' +
                ", \"credits\":" + credits +
                ", \"location\":\"" + location + '\"' +
                ", \"days\":\"" + days +
                '}';
    }
}

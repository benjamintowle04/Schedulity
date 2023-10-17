package onetomany.Activity;

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
public class Activity {

    @ApiModelProperty(notes = "Activity id", example = "1", required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //group id if recurring
    private int groupId;

    //name of the activity
    @ApiModelProperty(notes = "Activity name", example = "Code Club", required = true)
    private String actName;

    //loction of the activity
    @ApiModelProperty(notes = "Activity location", example = "Hall 772", required = true)
    private String location;

    //time this activity starts
    @ApiModelProperty(notes = "Activity Start Time", example = "18.0", required = true)
    private double startTime;

    //time this activity ends
    @ApiModelProperty(notes = "Activity End Time", example = "19.0", required = true)
    private double endTime;

    //whether the activity is recurring
    @ApiModelProperty(notes = "Whether the Activity is Re-Occuring", example = "true", required = true)
    private boolean recurring;

    //what days the activitiy takes place
    @ApiModelProperty(notes = "Date the Activity Takes Place", example = "T R", required = true)
    private String date;

    private String day;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user = new User();

    public Activity(){

    }

    public Activity(int groupId, String actName, String location, double startTime, double endTime, String date, boolean recurring, String day) {
        this.groupId = groupId;
        this.actName = actName;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.recurring = recurring;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate(){return date;}

    public void setDate(String date){this.date = date;}

    public boolean getRecurring(){return recurring;}

    public void setRecurring(boolean recurring){this.recurring = recurring;}

    public String getDay(){return day;}

    public void setDay(String day){this.day = day;}
}

package onetomany.SleepTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import onetomany.Courses.Course;
import onetomany.Users.User;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SleepTime {

    @ApiModelProperty(notes = "Sleep Time Id", example = "1", required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //hours for sleeping on weekdays
    @ApiModelProperty(notes = "Hours User Spends Sleeping on Week Nights", example = "9.0", required = true)
    private double weekdayHours;

    //time user goes to sleep on weekdays
    @ApiModelProperty(notes = "When the User goes to Sleep on a Week Day", example = "22.0", required = true)
    private double weekdayStartTime;

    //time user wakes up on weekdays
    @ApiModelProperty(notes = "When the User Wakes up on Week Day Mornings", example = "7.0", required = false)
    private double weekdayEndTime;

    //hours for sleeping on weekends
    @ApiModelProperty(notes = "Hours User Spends Sleeping on Week Ends", example = "10.0", required = true)
    private double weekendHours;

    //time user goes to sleep on weekends
    @ApiModelProperty(notes = "When the User goes to Sleep on a Week End", example = "1.0", required = true)
    private double weekendStartTime;

    //time user wakes up on weekends
    @ApiModelProperty(notes = "When the User Wakes up on Week End Mornings", example = "11.0", required = false)
    private double weekendEndTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public SleepTime(){

    }

    public SleepTime(double weekdayHours, double weekdayStartTime, double weekdayEndTime, double weekendHours, double weekendStartTime, double weekendEndTime) {

        this.weekdayHours = weekdayHours;
        this.weekdayStartTime = weekdayStartTime;
        this.weekdayEndTime = weekdayEndTime;

        this.weekendHours = weekendHours;
        this.weekendStartTime = weekendStartTime;
        this.weekendEndTime = weekendEndTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getWeekdayHours() {
        return weekdayHours;
    }

    public void setWeekdayHours(double weekdayHours) {
        this.weekdayHours = weekdayHours;
    }

    public double getWeekdayStartTime() {
        return weekdayStartTime;
    }

    public void setWeekdayStartTime(double weekdayStartTime) {
        this.weekdayStartTime = weekdayStartTime;
    }

    public double getWeekdayEndTime() {
        return weekdayEndTime;
    }

    public void setWeekdayEndTime(double weekdayEndTime) {
        this.weekdayEndTime = weekdayEndTime;
    }

    public double getWeekendHours() {
        return weekendHours;
    }

    public void setWeekendHours(double weekendHours) {
        this.weekendHours = weekendHours;
    }

    public double getWeekendStartTime() {
        return weekendStartTime;
    }

    public void setWeekendStartTime(double weekendStartTime) {
        this.weekendStartTime = weekendStartTime;
    }

    public double getWeekendEndTime() {
        return weekendEndTime;
    }

    public void setWeekendEndTime(double weekendEndTime) {
        this.weekendEndTime = weekendEndTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

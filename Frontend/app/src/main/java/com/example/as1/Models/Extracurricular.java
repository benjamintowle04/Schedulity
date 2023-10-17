package com.example.as1.Models;

public class Extracurricular{
    private String nameExtra;
    private double startTimeDouble;
    private double endTimeDouble;
    private String meetingDays;
    private String location;

    public Extracurricular(String name, double startTime, double endTime, String meetingDaysString,
                           String location) {

        this.nameExtra = name;
        this.startTimeDouble = startTime;
        this.endTimeDouble = endTime;
        this.meetingDays = meetingDaysString;
        this.location = location;
    }


    public String getName() {return this.nameExtra;}
    public double getStartTimeDouble() {return this.startTimeDouble;}
    public double getEndTimeDouble() {return this.endTimeDouble;}
    public String getMeetingDays() {return this.meetingDays;}
    public String getLocation() {return this.location;}

    public void setName(String name) {this.nameExtra = name;}
    public void setStartTimeDouble(double startTime) {this.startTimeDouble = startTime;}
    public void setEndTimeDouble(double endTime) {this.endTimeDouble = endTime;}
    public void setMeetingDays(String meetingDays) {this.meetingDays = meetingDays;}
    public void setLocation(String location) {this.location = location;}


}

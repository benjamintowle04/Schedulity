package com.example.as1.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class TempCourses {
    private int id;
    private double startTime;
    private double endTime;
    private String startDate;
    private String endDate;
    private String notes;
    private String course;
    private int credits;
    private String location;
    private String days;

    // Getter and setter methods for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and setter methods for startTime
    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    // Getter and setter methods for endTime
    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    // Getter and setter methods for startDate
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    // Getter and setter methods for endDate
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    // Getter and setter methods for notes
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Getter and setter methods for course
    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    // Getter and setter methods for credits
    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    // Getter and setter methods for location
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Getter and setter methods for days
    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    // Converts a JSON object into a TempCourses object
    public static TempCourses fromJson(JSONObject jsonObject) throws JSONException {
        TempCourses tempCourses = new TempCourses();
        tempCourses.setId(jsonObject.getInt("id"));
        tempCourses.setStartTime(jsonObject.getDouble("startTime"));
        tempCourses.setEndTime(jsonObject.getDouble("endTime"));
        tempCourses.setStartDate(jsonObject.getString("startDate"));
        tempCourses.setEndDate(jsonObject.getString("endDate"));
        tempCourses.setNotes(jsonObject.getString("notes"));
        tempCourses.setCourse(jsonObject.getString("course"));
        tempCourses.setCredits(jsonObject.getInt("credits"));
        tempCourses.setLocation(jsonObject.getString("location"));
        tempCourses.setDays(jsonObject.getString("days"));
        return tempCourses;
    }

    // Converts the TempCourses object into a JSON object
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("startTime", getStartTime());
            jsonObject.put("endTime", getEndTime());
            jsonObject.put("startDate", getStartDate());
            jsonObject.put("endDate", getEndDate());
            jsonObject.put("notes", getNotes());
            jsonObject.put("course", getCourse());
            jsonObject.put("credits", getCredits());
            jsonObject.put("location", getLocation());
            jsonObject.put("days", getDays());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

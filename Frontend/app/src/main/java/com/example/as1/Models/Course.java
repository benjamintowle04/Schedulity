package com.example.as1.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class Course {
    private double startTimeDouble;
    private double endTimeDouble;
    private int importance;
    private String course;
    private int credits;

//    private boolean lab;
    private String location;
    private String days;
    private double time;
    private int id;
    private String startDate;
    private String endDate;

    public Course(){}

    public Course(double startTime, double endTime, int importance, String course, int credits,
                   String location, String days, double time, int id) {

        this.startTimeDouble = startTime;
        this.endTimeDouble = endTime;
        this.importance = importance;
        this.course = course;
        this.credits = credits;
        this.location = location;
        this.days = days;
        this.time = time;
        this.id = id;
    }
    public Course(double startTime, double endTime, int importance, String course, int credits,
                  String location, String days, double time) {

        this.startTimeDouble = startTime;
        this.endTimeDouble = endTime;
        this.importance = importance;
        this.course = course;
        this.credits = credits;
//        this.lab = lab;
        this.location = location;
        this.days = days;
        this.time = time;
    }

    public Course(double startTime, double endTime, String course, int credits,
                  String location, String days, double time, int id) {

        this.startTimeDouble = startTime;
        this.endTimeDouble = endTime;
        this.course = course;
        this.credits = credits;
        this.location = location;
        this.days = days;
        this.time = time;
        this.id = id;
    }

    public Course(double startTime, double endTime, String course, int credits,
                  String location, String days) {

        this.startTimeDouble = startTime;
        this.endTimeDouble = endTime;
        this.course = course;
        this.credits = credits;
        this.location = location;
        this.days = days;

    }

    public static Course getCourse(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("UserObject",
                Context.MODE_PRIVATE);

        String userJson = preferences.getString("Course", "");
        return new Gson().fromJson(userJson, Course.class);
    }

    public double getStartTimeDouble() {
        return this.startTimeDouble;
    }

    public double getEndTimeDouble() {
        return this.endTimeDouble;
    }

    public int getImportance() {return this.importance;}

    public String getCourseName() {return this.course;}

    public int getCredits() {return this.credits;}

//    public boolean getLab() {return this.lab;}

    public String getLocation() {return this.location;}

    public String getDays() {return this.days;}

    public double getTime() {return this.time;}

    public static void saveCourse(Context context, Course course) {
        SharedPreferences preferences = context.getSharedPreferences("CourseObject",
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("User", new Gson().toJson(course));
        editor.commit();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStartTime(double startTime) {this.startTimeDouble = startTime;}
    public void setEndTime(double endTime) {this.endTimeDouble = endTime;}
    public void setImportance(int importance) {this.importance = importance;}
    public void setCredits(int credits) {this.credits = credits;}
    public void setLocation(String location) {this.location = location;}
    public void setDays(String days) {this.days = days;}
    public void setTime(double time) {this.time = time;}
    public void setCourseName(String courseName) {this.course = courseName;}
    public void setStartDate(String startDate) {this.startDate = startDate;}
    public void setEndDate(String endDate) {this.endDate = endDate;}
    public String getStartDate() {return this.startDate;}
    public String getEndDate() {return this.endDate;}



    public static Course fromJson(JSONObject json) throws JSONException {
        int id = json.getInt("id");
        double startTime = json.getDouble("startTime");
        double endTime = json.getDouble("endTime");
        int importance = json.getInt("importance");
        String course = json.getString("course");
        int credits = json.getInt("credits");
        String location = json.getString("location");
        String days = json.getString("days");
        double time = json.getDouble("time");

        return new Course(startTime, endTime, importance, course, credits, location, days, time);
    }

    /**
     *
     * @param json
     * @return name the string of the courseName (used for verifying the course ordering screen)
     * @throws JSONException
     */
    public static String fromJsonName(JSONObject json) throws JSONException {
        String name = json.getString("courseName");
        return name;
    }

    /**
     *
     * @param json
     * @return time the amount of study time for the course (used for studyHours Screen)
     * @throws JSONException
     */
    public static double fromJsonTime(JSONObject json) throws JSONException {
        double time = json.getDouble("time");
        return time;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("id", id);
        json.put("startTime", startTimeDouble);
        json.put("endTime", endTimeDouble);
        json.put("importance", importance);
        json.put("course", course);
        json.put("credits", credits);
        json.put("location", location);
        json.put("days", days);
        json.put("time", time);

        return json;
    }
}

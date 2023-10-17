package com.example.as1.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

public class CourseList {
    private List<Course> courses;

    public CourseList(List<Course> courses) {
        this.courses = courses;
    }

    public static CourseList getCourseList(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("CourseListObject", Context.MODE_PRIVATE);
        String userJson = preferences.getString("CourseList", "");
        return new Gson().fromJson(userJson, CourseList.class);
    }

    public static void saveCourseList(Context context, List<Course> list) {
        SharedPreferences preferences = context.getSharedPreferences("CourseListObject", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("CourseList", new Gson().toJson(list));
        editor.commit();
    }


    public List<Course> getCourses() {return this.courses;}

    public void addToList(Course added) {courses.add(added);
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}


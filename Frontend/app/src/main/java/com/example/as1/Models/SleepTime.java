package com.example.as1.Models;

public class SleepTime {
    private int weekendHours;
    private int weekdayHours;
    private double weekendStartTime;
    private double weekdayStartTime;

    public SleepTime(int weekendHours, int weeknightHours, double weekendTime, double weeknightTime) {
        this.weekendHours = weekendHours;
        this.weekdayHours = weeknightHours;
        this.weekendStartTime = weekendTime;
        this.weekdayStartTime = weeknightTime;
    }

    public int getWeekendHours() {return this.weekendHours;}
    public int getWeeknightHours() {return this.weekdayHours;}
    public double getWeekendTime() {return this.weekendStartTime;}
    public double getWeeknightTime() {return this.weekdayStartTime;}

    public void setWeekendHours(int weekendHours){this.weekendHours = weekendHours;}
    public void setWeeknightHours(int weeknightHours){this.weekdayHours = weeknightHours;}
    public void setWeekendTime(double weekendTime){this.weekendStartTime = weekendTime;}
    public void setWeeknightTime(double weeknightTime){this.weekdayStartTime = weeknightTime;}
}

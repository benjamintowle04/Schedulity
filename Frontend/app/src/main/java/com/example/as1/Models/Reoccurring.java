package com.example.as1.Models;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class Reoccurring implements Serializable {
    public Boolean isReoccurring;
    public Boolean isDaily;
    public Boolean isWeekly;
    public Boolean isMonthly;
    public Boolean isYearly;
    public LocalDate startDate;
    public LocalDate endDate;
    public List<DayOfWeek> dayOfWeekList;
}

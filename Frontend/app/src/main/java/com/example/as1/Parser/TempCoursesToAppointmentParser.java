package com.example.as1.Parser;

import com.example.as1.Enum.TypeOfAppointment;
import com.example.as1.Models.Appointment;
import com.example.as1.Models.TempCourses;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TempCoursesToAppointmentParser {

    public static List<Appointment> convert(TempCourses tempCourses) {
        List<Appointment> appointmentArrayList = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M-d-yyyy");
        LocalDate startDate = LocalDate.parse(tempCourses.getStartDate(), dateFormatter);
        LocalDate endDate = LocalDate.parse(tempCourses.getEndDate(), dateFormatter);

        LocalTime startTime = LocalTime.of((int) tempCourses.getStartTime(), 0);
        LocalTime endTime = LocalTime.of((int) tempCourses.getEndTime(), 0);

        List<DayOfWeek> days = new ArrayList<>();
        String[] daysArray = tempCourses.getDays().split(" ");
        for (String day : daysArray) {
            days.add(abbreviatedDayToDayOfWeek(day.toUpperCase()));
        }

        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
            if (days.contains(currentDate.getDayOfWeek())) {
                Appointment appointment = new Appointment(
                        tempCourses.getCourse(),
                        tempCourses.getId(),
                        currentDate,
                        startTime,
                        endTime,
                        days
                );
                appointment.setType(TypeOfAppointment.COURSE);
                appointmentArrayList.add(appointment);
            }
        }

        return appointmentArrayList;
    }

    private static DayOfWeek abbreviatedDayToDayOfWeek(String abbreviatedDay) {
        switch (abbreviatedDay) {
            case "M":
                return DayOfWeek.MONDAY;
            case "T":
                return DayOfWeek.TUESDAY;
            case "W":
                return DayOfWeek.WEDNESDAY;
            case "TH":
                return DayOfWeek.THURSDAY;
            case "F":
                return DayOfWeek.FRIDAY;
            case "S":
                return DayOfWeek.SATURDAY;
            case "SU":
                return DayOfWeek.SUNDAY;
            default:
                throw new IllegalArgumentException("Invalid day abbreviation: " + abbreviatedDay);
        }
    }
}

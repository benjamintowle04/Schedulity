package com.example.as1.Parser;

import com.example.as1.Models.Appointment;
import com.example.as1.Models.Course;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentCourseConverter {

        public static Course appointmentToCourse(Appointment appointment) {
            String courseName = appointment.getName();
            int id = appointment.getId();
            String days = appointment.getWeekDayString();
            String location = appointment.getLocation();
            double startTime = LocalTime.parse(appointment.getStartTime()).toSecondOfDay() / 3600.0;
            double endTime = LocalTime.parse(appointment.getEndTime()).toSecondOfDay() / 3600.0;
            int importance = appointment.getImportance();
            int credits = appointment.getCredits();

            return new Course(startTime, endTime, importance, courseName, credits, location, days, 0, id);
        }

        public static Appointment courseToAppointment(Course course, LocalDate date) {
            String name = course.getCourseName();
            int id = course.getId();
            String days = course.getDays();
            LocalTime startTime = LocalTime.of((int) course.getStartTimeDouble(), 0);
            LocalTime endTime = LocalTime.of((int) course.getEndTimeDouble(), 0);
            List<DayOfWeek> weekDays = new ArrayList<>();

            for (char day : days.toCharArray()) {
                switch (day) {
                    case 'M':
                        weekDays.add(DayOfWeek.MONDAY);
                        break;
                    case 'T':
                        weekDays.add(DayOfWeek.TUESDAY);
                        break;
                    case 'W':
                        weekDays.add(DayOfWeek.WEDNESDAY);
                        break;
                    case 'R':
                        weekDays.add(DayOfWeek.THURSDAY);
                        break;
                    case 'F':
                        weekDays.add(DayOfWeek.FRIDAY);
                        break;
                    case 'S':
                        weekDays.add(DayOfWeek.SATURDAY);
                        break;
                    case 'U':
                        weekDays.add(DayOfWeek.SUNDAY);
                        break;
                }
            }

            Appointment appointment = new Appointment(name, id, date, startTime, endTime, weekDays);
            appointment.setImportance(course.getImportance());
            appointment.setCredits(course.getCredits());
            appointment.setLocation(course.getLocation());
            appointment.setWeekDayString(course.getDays());

            return appointment;
        }
    }

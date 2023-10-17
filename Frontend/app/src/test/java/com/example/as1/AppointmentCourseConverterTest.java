package com.example.as1;

import static org.testng.Assert.assertEquals;

import com.example.as1.Models.Appointment;
import com.example.as1.Models.Course;
import com.example.as1.Parser.AppointmentCourseConverter;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class AppointmentCourseConverterTest {
    private Appointment appointment;
    private Course course;

    @Test
    public void testAppointmentToCourse() {

        appointment = new Appointment("Test Course", 1, LocalDate.now(), LocalTime.of(10, 0),
                LocalTime.of(12, 0), Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY));
        appointment.setImportance(2);
        appointment.setCredits(3);
        appointment.setLocation("Room 101");
        appointment.setWeekDayString("MW");

        Course convertedCourse = AppointmentCourseConverter.appointmentToCourse(appointment);

        assertEquals("Test Course", convertedCourse.getCourseName());
        assertEquals(1, convertedCourse.getId());
        assertEquals("Room 101", convertedCourse.getLocation());
        assertEquals(10.0, convertedCourse.getStartTimeDouble());
        assertEquals(12.0, convertedCourse.getEndTimeDouble());
        assertEquals(2, convertedCourse.getImportance());
        assertEquals(3, convertedCourse.getCredits());
        assertEquals("MW", convertedCourse.getDays());
    }

    @Test
    public void testCourseToAppointment() {
        course = new Course(10.0, 12.0, 2, "Test Course", 3, "Room 101", "MW", 0, 1);

        LocalDate date = LocalDate.now();
        Appointment convertedAppointment = AppointmentCourseConverter.courseToAppointment(course, date);

        assertEquals("Test Course", convertedAppointment.getName());
        assertEquals(1, convertedAppointment.getId());
        assertEquals(date.toString(), convertedAppointment.getDate());
        assertEquals(LocalTime.of(10, 0).toString(), convertedAppointment.getStartTime());
        assertEquals(LocalTime.of(12, 0).toString(), convertedAppointment.getEndTime());
        assertEquals(2, convertedAppointment.getImportance());
        assertEquals(3, convertedAppointment.getCredits());
        assertEquals("Room 101", convertedAppointment.getLocation());
        assertEquals("MW", convertedAppointment.getWeekDayString());

        List<DayOfWeek> expectedWeekDays = Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
        assertEquals(expectedWeekDays, convertedAppointment.getWeekDay());
    }
}

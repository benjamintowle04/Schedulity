package com.example.as1;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.example.as1.Enum.AccountType;
import com.example.as1.Enum.LoggedInStates;
import com.example.as1.Models.Appointment;
import com.example.as1.Models.User;
import com.example.as1.Parser.AppointmentParser;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class AppointmentParserTest {
    private User user;

    @Test
    public void testParseAppointments() {
        user = new User("JohnDoe",
                "password123",
                "john.doe@example.com",
                LoggedInStates.LOGGED_IN,
                AccountType.NORMAL_USER);

        List<DayOfWeek> days = Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
        Appointment appointmentList = new Appointment("Sample Appointment", 1,
                LocalDate.of(2023, 5, 1), LocalTime.of(10, 30), LocalTime.of(11, 30), days);
        user.addAppointmentToList(appointmentList);

        String input = "javascript:exportCsv('20230124','20230321','COM S 309','20230117','152000','20230512','161000','ATRB 1302','MO,WE,FR','COM S 327','20230118','093000','20230512','104500','LAGOMAR 1155','TU,TH','CYB E 234','20230118','154000','20230512','165500','COOVER 1011','TU,TH','S E 339','20230118','141000','20230512','152500','COOVER 2245','TU,TH');";
        List<Appointment> appointments = AppointmentParser.parseAppointments(input, user.appointmentList);

        assertEquals(149, appointments.size(), "Incorrect number of appointments parsed");

        for (Appointment appointment : appointments) {
            if (appointment.getName() == "COM S 309")
                assertTrue(appointment.getName().equals("COM S 309"), "Incorrect appointment name");
            else if (appointment.getName() == "COM S 327")
                assertTrue(appointment.getName().equals("COM S 327"), "Incorrect appointment name");
            else if (appointment.getName() == "CYB E 234")
                assertTrue(appointment.getName().equals("CYB E 234"), "Incorrect appointment name");
            else if (appointment.getName() == "S E 339")
                assertTrue(appointment.getName().equals("S E 339"), "Incorrect appointment name");
            else if (appointment.getName() == "Sample Appointment")
                assertTrue(appointment.getName().equals("Sample Appointment"), "Incorrect appointment name");

        }
    }

    @Test
    public void testDecipherDayString() {
        String dayString = "MoWeFr";
        String expected = "Mo,We,Fr";
        String actual = AppointmentParser.decipherDayString(dayString);
        assertEquals(expected, actual, "Incorrect deciphered day string");
    }
}

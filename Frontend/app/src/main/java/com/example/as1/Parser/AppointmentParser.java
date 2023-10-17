package com.example.as1.Parser;

import com.example.as1.Enum.TypeOfAppointment;
import com.example.as1.Models.Appointment;
import com.example.as1.Models.User;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentParser {

    public static List<Appointment> parseAppointments(String input, List<Appointment> appointmentList) {
        List<Appointment> appointments = new ArrayList<>();
        String tempToken = input.replace(",", "");
        String tempToken2 = tempToken.replace("''", "'");
        String[] tokens = tempToken2.split("'");
//        User user = User.getUser(context);
        int currentGroupId = User.GetHighestGroupId(appointmentList);

        for (int i = 3; i < tokens.length - 1; i += 7) {
            String name = tokens[i].trim();

            String startDateStr = tokens[i + 1].trim();
            LocalDate startDate = LocalDate.parse(startDateStr.substring(0, 4) + "-" + startDateStr.substring(4, 6) + "-" + startDateStr.substring(6));

            LocalTime startTime = LocalTime.parse(tokens[i + 2].substring(0, 2) + ":" + tokens[i + 2].substring(2, 4));

            String endDateStr = tokens[i + 3].trim();
            LocalDate endDate = LocalDate.parse(endDateStr.substring(0, 4) + "-" + endDateStr.substring(4, 6) + "-" + endDateStr.substring(6));

            LocalTime endTime = LocalTime.parse(tokens[i + 4].substring(0, 2) + ":" + tokens[i + 4].substring(2, 4));

            String location = tokens[i + 5].trim();
            String weekDays = tokens[i + 6].trim();

            String weekdays = decipherDayString(weekDays);
            String newWeekdays = dayToDayOfWeekOneChar(weekdays.split(","));
            List<DayOfWeek> days = (dayToDayOfWeek(newWeekdays.split(",")));

            for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
                Integer id = appointmentList.size();
                if (days.contains(currentDate.getDayOfWeek())) {
                    Appointment appointment = new Appointment(name, id, currentDate, startTime, endTime, days);
                    appointment.setLocation(location);
                    appointment.setWeekDayString(newWeekdays);
                    appointment.setRecurring(true);
                    appointment.setGroupId(currentGroupId);
                    appointment.setType(TypeOfAppointment.COURSE);
                    appointments.add(appointment);
                }
            }

            currentGroupId++;
        }

        return appointments;
    }

    public static String decipherDayString(String days) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < days.length(); i += 1) {
            sb.append(days.charAt(i));
            if (i % 2 == 1 && i < days.length() - 1) {
                sb.append(',');
            }
        }
        return sb.toString();
    }

    private static String dayToDayOfWeekOneChar(String[] days) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < days.length; i++) {
            switch (days[i].toUpperCase()) {
                case "MO":
                case "MONDAY":
                    result.append("M");
                    break;
                case "TU":
                case "TUESDAY":
                    result.append("T");
                    break;
                case "WE":
                case "WEDNESDAY":
                    result.append("W");
                    break;
                case "TH":
                case "THURSDAY":
                    result.append("R");
                    break;
                case "FR":
                case "FRIDAY":
                    result.append("F");
                    break;
                case "SA":
                case "SATURDAY":
                    result.append("S");
                    break;
                case "SU":
                case "SUNDAY":
                    result.append("U");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid day: " + days[i]);
            }
            if (i < days.length - 1) {
                result.append(",");
            }
        }
        return result.toString();
    }

    private static List<DayOfWeek> dayToDayOfWeek(String[] days) {
        List<DayOfWeek> result = new ArrayList<>();
        for (int i = 0; i < days.length; i++) {
            switch (days[i].toUpperCase()) {
                case "M":
                case "MO":
                case "MONDAY":
                    result.add(DayOfWeek.MONDAY);
                    break;
                case "T":
                case "TU":
                case "TUESDAY":
                    result.add(DayOfWeek.TUESDAY);
                    break;
                case "W":
                case "WE":
                case "WEDNESDAY":
                    result.add(DayOfWeek.WEDNESDAY);
                    break;
                case "R":
                case "TH":
                case "THURSDAY":
                    result.add(DayOfWeek.THURSDAY);
                    break;
                case "F":
                case "FR":
                case "FRIDAY":
                    result.add(DayOfWeek.FRIDAY);
                    break;
                case "S":
                case "SA":
                case "SATURDAY":
                    result.add(DayOfWeek.SATURDAY);
                    break;
                case "U":
                case "SU":
                case "SUNDAY":
                    result.add(DayOfWeek.SUNDAY);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid day: " + days[i]);
            }
        }
        return result;
    }

}

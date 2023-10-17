package com.example.as1.Parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LocalDateAndTimeParser {

    // Parse the startTime string to a LocalTime object
    public static LocalTime StringToTime(String time) {
//        Log.d("StringToTime", "Input time string: " + time);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(time, formatter);
    }

    public static LocalTime DoubleToTime(String time) {

        return doubleToTime(Double.parseDouble(time));
    }

    public static LocalTime doubleToTime(double time) {
        int hours = (int) time;
        int minutes = (int) Math.round((time - hours) * 60);
        return LocalTime.of(hours, minutes);
    }

    // Parse the date string to a LocalDate object
    public static LocalDate StringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    public static LocalDate StringToDateReversed(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M-d-yyyy");
        return LocalDate.parse(date, formatter);
    }

    public static double convertTimeToDouble(String timeString) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeFormat.setLenient(false);
        Date time = timeFormat.parse(timeString);

        int hours = time.getHours();
        int minutes = time.getMinutes();

        double timeDouble = hours + (minutes / 60.0);
        return timeDouble;
    }

}

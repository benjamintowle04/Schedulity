package com.example.as1.Utils;

import java.time.LocalTime;

public class TImeConversions {
    /**
     * Helper function for converting standard time to military time
     * @param time: Standard time in a string format with either "AM" or "PM" at the end
     * @return the value of the time in military form
     */
    public static double convertTimeToDouble(String time) {
        if (time.indexOf(':') == 1) { //If the time is in single digits
            time = '0' + time;
        }

        //Get Hours
        int h1 = (int)time.charAt(1) - '0';
        int h2 = (int)time.charAt(0) - '0';
        int hh = (h2 * 10 + h1 % 10);
        String newHours = time.substring(0, 2); //Initially set them the same and change if needed
        String minutes = time.substring(3, 5);

        //For time being AM
        if(time.charAt(time.length() - 2) == 'A') {
            if(hh == 12) { //Midnight is "00" in military
                newHours = "00";
            }
        }

        //For time being PM
        if(time.charAt(time.length() - 2) == 'P') {
            if (hh != 12) {
                hh += 12;
                newHours = String.valueOf(hh);
            }
        }

        return Double.parseDouble(newHours + "." + minutes);
    }

    public static String convertDoubleTimeToString(double time) {
        int hour = (int) time;
        int minute = (int) ((time - hour) * 100);

        if (hour == 0) {
            return String.format("12:%02d AM", minute);
        } else if (hour < 12) {
            return String.format("%d:%02d AM", hour, minute);
        } else if (hour == 12) {
            return String.format("12:%02d PM", minute);
        } else {
            return String.format("%d:%02d PM", hour - 12, minute);
        }
    }

    public static LocalTime doubleToLocalTime(double time) {
        int hours = (int) time;
        int minutes = (int) ((time - hours) * 100);
        return LocalTime.of(hours, minutes);
    }

}

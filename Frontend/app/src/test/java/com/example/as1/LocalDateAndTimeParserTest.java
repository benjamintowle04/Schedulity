package com.example.as1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.as1.Parser.LocalDateAndTimeParser;

import org.junit.Assert;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;

public class LocalDateAndTimeParserTest {

    @org.junit.Test
    public void addition_isCorrect() {
        Assert.assertEquals(4, 2 + 2);
    }

    @org.junit.Test
    public void testStringToTime() {
        LocalTime expectedTime = LocalTime.of(14, 30);
        LocalTime actualTime = LocalDateAndTimeParser.StringToTime("14:30");
        assertEquals(expectedTime, actualTime, "Time parsing failed");

        expectedTime = LocalTime.of(0, 0);
        actualTime = LocalDateAndTimeParser.StringToTime("00:00");
        assertEquals(expectedTime, actualTime, "Time parsing failed for midnight");
    }

    @org.junit.Test
    public void testStringToTimeInvalid() {
        assertThrows(RuntimeException.class, () -> LocalDateAndTimeParser.DoubleToTime("25:00"), "Invalid time format accepted");
    }

    @org.junit.Test
    public void testStringToDate() {
        LocalDate expectedDate = LocalDate.of(2023, 4, 30);
        LocalDate actualDate = LocalDateAndTimeParser.StringToDate("2023-04-30");
        assertEquals(expectedDate, actualDate, "Date parsing failed");

        expectedDate = LocalDate.of(2000, 1, 1);
        actualDate = LocalDateAndTimeParser.StringToDate("2000-01-01");
        assertEquals(expectedDate, actualDate, "Date parsing failed for 2000-01-01");
    }


    @org.junit.Test
    public void testConvertTimeToDouble() throws ParseException {
        double expectedTime = 14.5;
        double actualTime = LocalDateAndTimeParser.convertTimeToDouble("14:30");
        assertEquals(expectedTime, actualTime, 0.01, "Time conversion to double failed");

        expectedTime = 0;
        actualTime = LocalDateAndTimeParser.convertTimeToDouble("00:00");
        assertEquals(expectedTime, actualTime, 0.01, "Time conversion to double failed for midnight");
    }

    @org.junit.Test
    public void testConvertTimeToDoubleInvalid() {
        assertThrows(ParseException.class, () -> LocalDateAndTimeParser.convertTimeToDouble("25:00"), "Invalid time format accepted");
    }

    public class Logger {
        public int d(String tag, String msg) {
            return android.util.Log.d(tag, msg);
        }
    }
}

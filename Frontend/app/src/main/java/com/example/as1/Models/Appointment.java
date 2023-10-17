package com.example.as1.Models;

import com.example.as1.Enum.TypeOfAppointment;
import com.example.as1.Parser.LocalDateAndTimeParser;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Appointment implements Serializable {
    private Integer id;
    private Integer groupId;
    private String name;
    private String date;
    private String startTime;
    private String endTime;
    private String notes;
    private String location;
    private String weekDayString;
    private List<DayOfWeek> weekDay;
    private int importance;
    private int credits;
    private boolean recurring;
    private TypeOfAppointment type;

    public Appointment(String name, Integer id, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.date = date.toString();
        this.startTime = startTime.toString();
        this.endTime = endTime.toString();
        this.id = id;
    }


    public Appointment(String name, Integer id, LocalDate date, LocalTime startTime, LocalTime endTime, List<DayOfWeek> days) {
        this.name = name;
        this.date = date.toString();
        this.startTime = startTime.toString();
        this.endTime = endTime.toString();
        this.weekDay = days;
        this.id = id;
        this.recurring = true;
    }

    public static ArrayList<Appointment> appointmentsForDate(List<Appointment> appointmentList, LocalDate date) {
        ArrayList<Appointment> appointments = new ArrayList<>();
        if (date == null) {
            return new ArrayList<Appointment>();
        }
        if (appointmentList != null) {
            for (Appointment appointment : appointmentList) {
                LocalDate localDate = null;
//                if (appointment.recurring) {
//                    //check if reoccurring in this time window
//                    LocalDate startDate = LocalDateAndTimeParser.StringToDate(appointment.getStartDate());
//                    LocalDate endDate = LocalDateAndTimeParser.StringToDate(appointment.getEndDate());
//                    if (startDate.isBefore(date) && endDate.isAfter(date)) {
//                        //thats good
//                        DayOfWeek selectedDate = date.getDayOfWeek();
//                        for (DayOfWeek day : appointment.weekDay) {
//                            if (selectedDate == day) {
//                                appointments.add(appointment);
//                            }
//                        }
//                    }
//                } else
                if (appointment.getDate() != null) {
                    localDate = LocalDateAndTimeParser.StringToDate(appointment.getDate());
                }
                if (localDate != null && date != null && localDate.equals(date)) {
                    appointments.add(appointment);
                }
            }
        }

        return appointments;
    }

    public static ArrayList<Appointment> appointmentsForDateAndTime(List<Appointment> appointmentList, LocalDate date, LocalTime time) {
        ArrayList<Appointment> appointments = new ArrayList<>();

        if (appointmentList != null) {
            for (Appointment appointment : appointmentList) {
                LocalTime tempStartTime = LocalDateAndTimeParser.StringToTime(appointment.startTime);
                LocalTime tempEndTime = LocalDateAndTimeParser.StringToTime(appointment.endTime);
                int eventStartHour = tempStartTime.getHour();
                int eventEndHour = tempEndTime.getHour();
                int cellHour = time.getHour();
                LocalDate localDate = null;
                if (appointment.getDate() != null) {
                    localDate = LocalDateAndTimeParser.StringToDate(appointment.getDate());
                }


                if (localDate != null && localDate.equals(date) && eventStartHour <= cellHour && eventEndHour >= cellHour){
//                    if(eventStartHour != cellHour )
//                        appointment.setName("");
                    appointments.add(appointment);

                }
            }
        }
        return appointments;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeekDayString() {
        return weekDayString;
    }

    public void setWeekDayString(String weekDayString) {
        this.weekDayString = weekDayString;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public List<DayOfWeek> getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(List<DayOfWeek> weekDay) {
        this.weekDay = weekDay;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public TypeOfAppointment getType() {
        return type;
    }

    public void setType(TypeOfAppointment type) {
        this.type = type;
    }

    public static Appointment fromJson(JSONObject jsonObject) {
        try {
            String name = jsonObject.getString("actName");

            int id = Integer.parseInt(jsonObject.getString("id"));
            String dateString = jsonObject.getString("date");
            String startTimeString = jsonObject.getString("startTime");
            String endTimeString = jsonObject.getString("endTime");

            LocalDate date = LocalDateAndTimeParser.StringToDateReversed(dateString);
            LocalTime startTime = LocalDateAndTimeParser.DoubleToTime(startTimeString);
            LocalTime endTime = LocalDateAndTimeParser.DoubleToTime(endTimeString);

            Appointment tempAppointment = new Appointment(name, id, date, startTime, endTime);
            tempAppointment.setType(TypeOfAppointment.APPOINTMENT);


            return tempAppointment;
        } catch (Exception e) {
            System.err.println("Error parsing appointment from JSON: " + e.getMessage());
            return null;
        }
    }

    public static List<Appointment> splitAppointmentsByHour(Appointment appointment) {
        List<Appointment> splitAppointments = new ArrayList<>();
        Boolean indicator = false;
        LocalDate date = LocalDateAndTimeParser.StringToDate(appointment.getDate());
        LocalTime startTime = LocalDateAndTimeParser.StringToTime(appointment.getStartTime());
        LocalTime endTime = LocalDateAndTimeParser.StringToTime(appointment.getEndTime());
        long durationInMinutes = Duration.between(startTime, endTime).toMinutes();

        if (durationInMinutes <= 60) {
            splitAppointments.add(appointment);
            return splitAppointments;
        }

        while (durationInMinutes > 0) {
            if (indicator) {
                startTime = startTime.plusMinutes(1);
            }

            LocalTime nextHour = startTime.plusHours(1).truncatedTo(ChronoUnit.HOURS);
            if (startTime.getMinute() > 0 && startTime.getHour() == nextHour.getHour()) {
                nextHour = startTime.plusMinutes(60 - startTime.getMinute());
            }

            if (nextHour.isAfter(endTime) || nextHour.equals(endTime)) {
                nextHour = endTime;
            }

            Appointment splitAppointment = new Appointment(appointment.getName(), appointment.getId(), date, startTime, nextHour);
            splitAppointment.setNotes(appointment.getNotes());
            splitAppointment.setLocation(appointment.getLocation());
            splitAppointment.setImportance(appointment.getImportance());
            splitAppointment.setCredits(appointment.getCredits());
            splitAppointment.setType(appointment.getType());
            splitAppointments.add(splitAppointment);

            if (indicator) {
                startTime = startTime.minusMinutes(1);
            }

            durationInMinutes -= Duration.between(startTime, nextHour).toMinutes();
            startTime = nextHour;
            indicator = true;
        }

        return splitAppointments;
    }

    // custom Serializer for LocalDate field
    public static class LocalDateSerializer implements JsonSerializer<LocalDate> {
        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(DATE_FORMATTER.format(date));
        }
    }

    // custom Deserializer for LocalDate field
    public static class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDate.parse(json.getAsString(), DATE_FORMATTER);
        }
    }

    // custom Serializer for LocalTime field
    public static class LocalTimeSerializer implements JsonSerializer<LocalTime> {
        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

        @Override
        public JsonElement serialize(LocalTime time, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(TIME_FORMATTER.format(time));
        }
    }

    // custom Deserializer for LocalTime field
    public static class LocalTimeDeserializer implements JsonDeserializer<LocalTime> {
        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

        @Override
        public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalTime.parse(json.getAsString(), TIME_FORMATTER);
        }
    }
}
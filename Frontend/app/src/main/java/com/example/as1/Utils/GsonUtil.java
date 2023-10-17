package com.example.as1.Utils;

import com.example.as1.Models.Appointment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import java.time.LocalTime;

public class GsonUtil {

    public static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(LocalDate.class, new Appointment.LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new Appointment.LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new Appointment.LocalTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new Appointment.LocalTimeDeserializer());

        return gsonBuilder.create();
    }
}

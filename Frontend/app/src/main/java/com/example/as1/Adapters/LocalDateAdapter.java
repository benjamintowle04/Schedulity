package com.example.as1.Adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(DATE_FORMAT.format(value));
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        if (in != null) {
            return LocalDate.parse(in.nextString(), DATE_FORMAT);
        } else {
            return null;
        }
    }
}

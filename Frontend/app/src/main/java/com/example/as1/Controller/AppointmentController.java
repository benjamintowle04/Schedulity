package com.example.as1.Controller;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.Models.Appointment;
import com.example.as1.Models.User;
import com.example.as1.Parser.LocalDateAndTimeParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentController {

    public void createAppointment(Context context, Appointment appointment, String url) {
        JSONObject json = new JSONObject();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        try {
            // Convert Appointment object to JSON object
            json.put("id", appointment.getId());
            if (appointment.getGroupId() != null)
                json.put("groupId", appointment.getGroupId());
            json.put("name", appointment.getName());
            String date = dateFormatter.format(LocalDateAndTimeParser.StringToDate(appointment.getDate())).toString().replace("\\", "");
            json.put("date", date);
            json.put("startTime", LocalDateAndTimeParser.convertTimeToDouble(appointment.getStartTime()));
            json.put("endTime", LocalDateAndTimeParser.convertTimeToDouble(appointment.getEndTime()));
            json.put("notes", appointment.getNotes());
            json.put("location", appointment.getLocation());
            json.put("credits", appointment.getCredits());
            json.put("recurring", appointment.isRecurring());
//            if (appointment.getType() != TypeOfAppointment.COURSE) {
//                json.put("type", appointment.getType().toString());
//            }
            if (appointment.getWeekDay() != null) {
                json.put("weekDay", appointment.getWeekDay().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        sendPostRequest(context, json, url);
    }

    public void updateAppointment(Context context, Appointment appointment, String url) {
        JSONObject json = new JSONObject();
        try {
            // Convert Appointment object to JSON object
            json.put("id", appointment.getId());
            json.put("name", appointment.getName());
            json.put("date", appointment.getDate());
            json.put("startTime", appointment.getStartTime());
            json.put("endTime", appointment.getEndTime());
            json.put("notes", appointment.getNotes());
            json.put("location", appointment.getLocation());
            json.put("importance", appointment.getImportance());
            json.put("credits", appointment.getCredits());
            json.put("recurring", appointment.isRecurring());
            json.put("type", appointment.getType().toString());
            if (appointment.getWeekDay() != null) {
                json.put("weekDay", appointment.getWeekDay().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        sendPostRequest(context, json, url);
    }

    public void deleteAppointment(Context context, int appointmentId, String url) {
        JSONObject json = new JSONObject();
        try {
            // Convert Appointment ID to JSON object
            json.put("id", appointmentId);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        sendPostRequest(context, json, url);
    }

    private void sendPostRequest(Context context, JSONObject json, String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String message = response.optString("message");
                        if (message.equals("success")) {
                            Toast.makeText(context, "Appointment operation successful", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Error in Appointment operation", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        error.printStackTrace();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(request);
    }

    public void getAppointmentById(Context context, int appointmentId, String url, Response.Listener<JSONObject> onResponse, Response.ErrorListener onError) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                onResponse,
                onError
        );

        // Add the request to the RequestQueue.
        queue.add(request);
    }

    public void getAppointmentsByDate(Context context, LocalDate date, String url, Response.Listener<JSONObject> onResponse, Response.ErrorListener onError) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                onResponse,
                onError
        );

        // Add the request to the RequestQueue.
        queue.add(request);
    }

    public void getAppointmentsByTime(Context context, LocalTime startTime, LocalTime endTime, String url, Response.Listener<JSONObject> onResponse, Response.ErrorListener onError) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                onResponse,
                onError
        );

        // Add the request to the RequestQueue.
        queue.add(request);
    }

    public void getAppointmentsByWeekday(Context context, String weekday, String url, Response.Listener<JSONObject> onResponse, Response.ErrorListener onError) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                onResponse,
                onError
        );

        // Add the request to the RequestQueue.
        queue.add(request);
    }

    public void sendAppointments(Context context, List<Appointment> appointments, String url) {
        JSONArray jsonArray = new JSONArray();

        for (Appointment appointment : appointments) {
            JSONObject json = new JSONObject();
            try {
                // Convert Appointment object to JSON object
                json.put("name", appointment.getName());
                json.put("date", appointment.getDate());
                json.put("startTime", appointment.getStartTime());
                json.put("endTime", appointment.getEndTime());
                json.put("notes", appointment.getNotes());
                json.put("location", appointment.getLocation());
                json.put("importance", appointment.getImportance());
                json.put("credits", appointment.getCredits());
                json.put("recurring", appointment.isRecurring());
                json.put("type", appointment.getType().toString());
                if (appointment.getWeekDay() != null) {
                    json.put("weekDay", appointment.getWeekDay().toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            jsonArray.put(json);
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.POST,
                url,
                jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(context, "Appointments sent successfully", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(request);
    }

    public void getAppointments(Context context, String url) {
        User user = User.getUser(context);

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    // Handle the successful response here
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject appointmentJson = response.getJSONObject(i);
                            Appointment appointment = Appointment.fromJson(appointmentJson);
                            if (!user.appointmentList.contains(appointment)) {
                                user.addAppointmentToList(appointment);
                            }
                        } catch (Exception e) {
                            System.err.println("Error parsing appointment: " + e.getMessage());
                        }
                    }
                    user.saveUser(context);
                },
                error -> {
                    // Handle the error response here
                    System.err.println("Error: " + error.getMessage());
                }
        );
        // Add the request to the RequestQueue.
        queue.add(request);
    }
}

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
import com.example.as1.Models.TempCourses;
import com.example.as1.Models.User;
import com.example.as1.Parser.TempCoursesToAppointmentParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TempCoursesController {

    public void createTempCourse(Context context, TempCourses tempCourse, String url) {
        JSONObject json = new JSONObject();

        try {
            // Convert TempCourses object to JSON object
            json.put("id", tempCourse.getId());
            json.put("startTime", tempCourse.getStartTime());
            json.put("endTime", tempCourse.getEndTime());
            json.put("startDate", tempCourse.getStartDate());
            json.put("endDate", tempCourse.getEndDate());
            json.put("notes", tempCourse.getNotes());
            json.put("course", tempCourse.getCourse());
            json.put("credits", tempCourse.getCredits());
            json.put("location", tempCourse.getLocation());
            json.put("days", tempCourse.getDays());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        sendPostRequest(context, json, url);
    }

    public void updateTempCourse(Context context, TempCourses tempCourse, String url) {
        JSONObject json = new JSONObject();

        try {
            // Convert TempCourses object to JSON object
            json.put("id", tempCourse.getId());
            json.put("startTime", tempCourse.getStartTime());
            json.put("endTime", tempCourse.getEndTime());
            json.put("startDate", tempCourse.getStartDate());
            json.put("endDate", tempCourse.getEndDate());
            json.put("notes", tempCourse.getNotes());
            json.put("course", tempCourse.getCourse());
            json.put("credits", tempCourse.getCredits());
            json.put("location", tempCourse.getLocation());
            json.put("days", tempCourse.getDays());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        sendPutRequest(context, json, url);
    }

    public void deleteTempCourse(Context context, int tempCourseId, String url) {
        JSONObject json = new JSONObject();
        try {
            // Convert TempCourses ID to JSON object
            json.put("id", tempCourseId);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        sendDeleteRequest(context, json, url);
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
                            Toast.makeText(context, "TempCourse operation successful", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Error in TempCourse operation", Toast.LENGTH_LONG).show();
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

    private void sendPutRequest(Context context, JSONObject json, String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String message = response.optString("message");
                        if (message.equals("success")) {
                            Toast.makeText(context, "TempCourse operation successful", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Error in TempCourse operation", Toast.LENGTH_LONG).show();
                        }
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

    private void sendDeleteRequest(Context context, JSONObject json, String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String message = response.optString("message");
                        if (message.equals("success")) {
                            Toast.makeText(context, "TempCourse operation successful", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Error in TempCourse operation", Toast.LENGTH_LONG).show();
                        }
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

    public void getTempCourseById(Context context, int tempCourseId, String url, Response.Listener<JSONObject> onResponse, Response.ErrorListener onError) {
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

    public void getTempCourses(Context context, String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    // Handle the successful response here
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject tempCoursesJson = response.getJSONObject(i);
                            TempCourses tempCourses = TempCourses.fromJson(tempCoursesJson);
                            // Do something with the tempCourses object here


                            List<Appointment> newAppointmentList = TempCoursesToAppointmentParser.convert(tempCourses);
                            User user = User.getUser(context);
                            
                            if(user.appointmentList == null){
                                user.appointmentList = new ArrayList<>();
                            }
                            user.addAppointmentListToList(newAppointmentList);
                            user.saveUser(context);
                        } catch (Exception e) {
                            System.err.println("Error parsing TempCourses: " + e.getMessage());
                        }
                    }
                },
                error -> {
                    // Handle the error response here
                    System.err.println("Error: " + error.getMessage());
                }
        );

        // Add the request to the RequestQueue.
        queue.add(request);
    }


    public void sendTempCourses(Context context, List<TempCourses> tempCourses, String url) {
        JSONArray jsonArray = new JSONArray();

        for (TempCourses tempCourse : tempCourses) {
            JSONObject json = new JSONObject();
            try {
                // Convert TempCourses object to JSON object
                json.put("startTime", tempCourse.getStartTime());
                json.put("endTime", tempCourse.getEndTime());
                json.put("startDate", tempCourse.getStartDate());
                json.put("endDate", tempCourse.getEndDate());
                json.put("notes", tempCourse.getNotes());
                json.put("course", tempCourse.getCourse());
                json.put("credits", tempCourse.getCredits());
                json.put("location", tempCourse.getLocation());
                json.put("days", tempCourse.getDays());
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
                        Toast.makeText(context, "TempCourses sent successfully", Toast.LENGTH_LONG).show();
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
}

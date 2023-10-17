package com.example.as1.Controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.Adapters.CourseAdapter;
import com.example.as1.AddExtrasActivity;
import com.example.as1.AddExtrasDetailsActivity;
import com.example.as1.Models.Extracurricular;
import com.example.as1.Models.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityController {

    public void getActivityList(Context context, String url, RecyclerView recyclerView) {
        User user = User.getUser(context);
        int id = user.getId();
        url = url + "/" + id;
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<String> activities = new ArrayList<String>();
                    Extracurricular activity;
                   for (int i = 0; i < response.length(); i++) {
                       JSONObject activityJson = response.getJSONObject(i);
                       if (activityJson.getBoolean("recurring")) {
                           String name = activityJson.getString("actName");
                           double start = activityJson.getDouble("startTime");
                           double end = activityJson.getDouble("endTime");
                           String location = activityJson.getString("location");
                           String days = activityJson.getString("day");

                           activity = new Extracurricular(name, start, end, days, location);
                           activities.add(activity.getName());
                       }
                   }
                   //Setting adapter
                    CourseAdapter adapter = new CourseAdapter(context, activities);
                   recyclerView.setAdapter(adapter);

                    adapter.setOnItemCLickListener(new CourseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(context,
                                    AddExtrasDetailsActivity.class);

                            intent.putExtra("index", position);
                            context.startActivity(intent);
                        }
                    });

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);
    }

    public void postActivity(Context context, String url, Extracurricular activity) {
        User user = User.getUser(context);
        int id = user.getId();
        url = url + "/" + id;
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject json = new JSONObject();
        try {
            // Convert object to JSON Object
            json.put("actName", activity.getName());
            json.put("startTime", activity.getStartTimeDouble());
            json.put("endTime", activity.getEndTimeDouble());
            json.put("location", activity.getLocation());
            json.put("date", "8-25-2023");
            json.put("recurring", true);
            json.put("day", activity.getMeetingDays());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());
                    if (response.getString("message") != null && response.getString("message").equals("success")) {
                        System.out.println("successful post");
                        String message = "Successful add";
                        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                        toast.show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
}

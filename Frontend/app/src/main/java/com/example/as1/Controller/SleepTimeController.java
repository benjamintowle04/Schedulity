package com.example.as1.Controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.Models.SleepTime;
import com.example.as1.Models.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class SleepTimeController {

    public void postSleepTIme(Context context, SleepTime sleepTime, String url) {
        User user = User.getUser(context);
        int id = user.getId();
        url = url + "/" + id;
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject json;
        try {
            // Convert object to JSON Object
            json = new JSONObject(new Gson().toJson(sleepTime));
            json.put("id", id);
            json.put("weekdayEndTime", 0);
            json.put("weekendEndTime", 0);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                System.out.println("successful post");
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

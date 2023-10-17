package com.example.as1.Controller;

import android.app.Application;
import android.content.Context;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.Enum.LoggedInStates;
import com.example.as1.Models.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class MyAppController extends Application {

    public void sendPostRequest(Context context, Object object, String url, TextView textview) {
        JSONObject json = null;
        try {
            // Convert object to JSON object
            json = new JSONObject(new Gson().toJson(object));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        System.out.println(response.toString());
                        if (textview != null) {
                            textview.setText(response.toString());
                        }

                        if (object instanceof User) {
                            String message = response.optString("message");
                            if ("Successful Login".equals(message)) {
                                User user = User.getUser(context);
                                user.setType(LoggedInStates.LOGGED_IN);
                                User.saveUser(context, user);
                            }
                            else{
                                User user = User.getUser(context);
                                user.setType(LoggedInStates.NOT_LOGGED_IN);
                                User.saveUser(context, user);
                            }
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        error.printStackTrace();

                    }
                });

        // Add the request to the RequestQueue.
        queue.add(request);
    }




    public <T> T sendPostRequestWithResponse(Context context, Object object, String url, Class<T> responseType) {
        JSONObject json = null;
        try {
            // Convert object to JSON object
            json = new JSONObject(new Gson().toJson(object));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        // Create a new RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);

        // Create a new CountDownLatch with a count of 1
        CountDownLatch latch = new CountDownLatch(1);

        // Create a new AtomicReference to hold the response
        AtomicReference<T> responseHolder = new AtomicReference<>();

        // Create a new JsonObjectRequest with a success and error listener
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        System.out.println(response.toString());
                        // Parse the response into the specified type
                        T parsedResponse = new Gson().fromJson(response.toString(), responseType);
                        // Set the responseHolder with the parsed response
                        responseHolder.set(parsedResponse);
                        // Count down the latch to release the main thread
                        latch.countDown();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        error.printStackTrace();
                        // Set the responseHolder to null
                        responseHolder.set(null);
                        // Count down the latch to release the main thread
                        latch.countDown();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(request);

        try {
            // Wait for the latch to count down
            latch.await();
            // Return the response from the responseHolder
            return responseHolder.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

}

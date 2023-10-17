package com.example.as1.Controller;

import static com.example.as1.Utils.CalendarUtils.selectedDate;
import static com.example.as1.Utils.URL.URL_GET_ONE_USER;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.Adapters.FriendsAdapter;

import android.widget.ListView;
import android.widget.Toast;

import com.example.as1.Adapters.HourAdapter;
import com.example.as1.AddFriendsActivity;
import com.example.as1.CourseSummaryActivity;
import com.example.as1.DailyViewActivity;
import com.example.as1.Enum.AccountType;
import com.example.as1.Models.Appointment;
import com.example.as1.Models.Friend;
import com.example.as1.Models.HourEvent;
import com.example.as1.Models.User;
import com.example.as1.R;
import com.example.as1.Utils.CalendarUtils;
import com.example.as1.Utils.TImeConversions;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FriendsController extends Application {
    public void sendGetRequest(Context context, String url, ListView listView, SearchView searchView, Boolean delete) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Friend> friendsList = new ArrayList<>();
                        User user = User.getUser(context);
                        List<Friend> currentFriend = new ArrayList<Friend>();
                        List<Integer> currentFriendIds = new ArrayList<Integer>();
                        if (user != null && user.getFriendsList() != null) {
                            currentFriend = user.getFriendsList();
                            for (int i = 0; i < currentFriend.size(); i++) {//Collecting id numbers from all friends in the list
                                currentFriendIds.add(currentFriend.get(i).getId());
                            }
                        }

                        try {
                            // Loop through the JSON array and create a Friend object for each item
                               for (int i = 0; i < response.length(); i++) {
                                   JSONObject friendObj = response.getJSONObject(i);
                                   Friend friend = new Friend();
                                   friend.setId(friendObj.getInt("id"));
                                   friend.setUsername(friendObj.getString("username"));
                                   friend.setEmail(friendObj.getString("email"));

                                   // Add the Friend object to the list
                                   if (friend.getId() != User.getUser(context).getId()) {
                                       if ((currentFriend != null && currentFriendIds.contains(friend.getId()))
                                               || context instanceof AddFriendsActivity) {
                                           friendsList.add(friend);
                                       }
                                   }
                               }

                            // Pass the studentList to the adapter and set it to the ListView
                            FriendsAdapter adapter = new FriendsAdapter(context, friendsList);
                            listView.setAdapter(adapter);
                            adapter.setDelete(delete);
                            adapter.initSearchView(searchView);

                        } catch (JSONException e) {
                            e.printStackTrace();
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


    public void sendPostRequest(Context context, Integer id, String url, Boolean delete) {
        JSONObject json = new JSONObject();
        try {
            // Convert object to JSON object
            json.put("id", id.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("JSON string:\n " + json.toString());
        Log.d("FriendsController", "sent json: " + json.toString());

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        String message;
                        if (delete) {
                            message = "Deleted successfully ";
                        } else {
                            message = "Added successfully";
                        }

                        Log.d("FriendsController", "in onResponse method!");
                        User user = User.getUser(context);

                        String responseString = response.optString("message");

                        if (responseString.equals("success")) {
                            System.out.println("Successfully Added");
                            UserController controller = new UserController();
                            String url = URL_GET_ONE_USER.replace("{id}", id.toString());
                            controller.sendGetRequest(context, url);
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            User.saveUser(context, user);

                        } else {
                            System.out.println("ERROR!!");
                            Log.d("FriendsController", "NO USER FOUND");
                            Toast.makeText(context, "Error in Friends", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR!!");
                        Log.d("FriendsController", "NO USER FOUND");
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();

                    }
                });

        // Add the request to the RequestQueue.
        queue.add(request);
    }

    public void sendGetRequestObject(Context context, String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        System.out.println("SUCCESS!!");
                        Log.d("FriendsController", "in onResponse method!");
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

    public void getFriendDayRequest(Context context, String url, ListView hourListView) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response);
                Appointment appt;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject eventJson = response.getJSONObject(i);
                        String name = eventJson.getString("name");
                        double start = eventJson.getDouble("startTime");
                        double end = eventJson.getDouble("endTime");

//                        appt = new Appointment(name, CalendarUtils.selectedDate,
//                                TImeConversions.doubleToLocalTime(start),
//                                TImeConversions.doubleToLocalTime(end));
//
//                        System.out.println(appt.getName());
                        //TODO
//                        Appointment.eventsList.add(appt);

                    }

                    catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                //Making the Hour Event list
                ArrayList<HourEvent> hourEventlist = new ArrayList<>();
                for (int hour = 0; hour < 24; hour++) {
                    LocalTime time = LocalTime.of(hour, 0);
                    //ArrayList<Appointment> events = Appointment.eventsForDateAndTime(selectedDate, time);
                    //HourEvent hourEvent = new HourEvent(time, events);
                    //hourEventlist.add(hourEvent);
                    //TODO: Uncomment when me and robert figure out how to fix this shit
                }

                //Setting the adapter
                HourAdapter hourAdapter = new HourAdapter(context, hourEventlist);
                hourListView.setAdapter(hourAdapter);

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

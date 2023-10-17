package com.example.as1.Controller;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.Adapters.CourseAdapter;
import com.example.as1.AddClassesActivity;
import com.example.as1.AddClassesDetailsActivity;
import com.example.as1.Models.Course;
import com.example.as1.Models.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseController {


    public void sendPostRequestCourse(Context context, Course course, String url, String start, String end) {
        JSONObject json = new JSONObject();
        User user = User.getUser(context);
        if (user == null && user.getId() == null) {
           return;
        }
        String id = String.valueOf(user.getId());
        url = url + "/" + id;

        try {
            // Convert object to JSON Object
            json.put("startTime", course.getStartTimeDouble());
            json.put("endTime", course.getEndTimeDouble());
            json.put("course", course.getCourseName());
            json.put("location", course.getLocation());
            json.put("credits", course.getCredits());
            json.put("course", course.getCourseName());
            json.put("startDate", start);
            json.put("endDate", end);
            json.put("days", course.getDays());
            System.out.println(json);
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
                            System.out.println("successful parsed");
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


    public void sendPostRequestCourseList(Context context, Object object, String url) {
        JSONArray json = null;
        User user = User.getUser(context);
        String id = String.valueOf(user.getId());
        url = url + "/" + id;

        int requestLength;

        try {
            // Convert object to JSON object
            json = new JSONArray(new Gson().toJson(object));
            for (int i = 0; i < json.length(); i++) {
                JSONObject courseJson = json.getJSONObject(i);
                courseJson.put("notes", " ");
            }
            requestLength = json.length();
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }


        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle successful response
                        System.out.println(response.toString());

                        String displayText = "";
                        try {
                            if (response.length() > requestLength){
                                for (int i = 1; i < response.length(); i++) {
                                    JSONObject courseJson = response.getJSONObject(i);

                                    String name = Course.fromJsonName(courseJson);
                                    displayText += i + ": " + name + "  ";
                                }

                            }

                            else {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject courseJson = response.getJSONObject(i);

                                    String name = Course.fromJsonName(courseJson);
                                    displayText += (i+1) + ": " + name + "  ";
                                }
                            }

                            Toast toast = Toast.makeText(context, displayText,
                                    Toast.LENGTH_LONG);
                            toast.show();


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

    public void sendGetCourseListRequest(Context context, String url, int friendId, RecyclerView recyclerView) {
        RequestQueue queue = Volley.newRequestQueue(context);
        url = url + "/" + friendId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<String> courseListNames = new ArrayList<String>();
                Course currentCourse;
                System.out.println(response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject json = response.getJSONObject(i);
                        String name = json.getString("course");
                        int id = json.getInt("id");
                        double startTime = json.getDouble("startTime");
                        double endTime = json.getDouble("endTime");
                        int credits = json.getInt("credits");
                        String days = json.getString("days");
                        String location = json.getString("location");

                        currentCourse = new Course(startTime, endTime, name, credits, location, days, 0, id);
                        courseListNames.add(currentCourse.getCourseName() + " courseId: " + id);
                    }
                    CourseAdapter adapter = new CourseAdapter(context, courseListNames);
                    recyclerView.setAdapter(adapter);

                    ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                            ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START
                                    | ItemTouchHelper.END, 0) {
                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView,
                                              @NonNull RecyclerView.ViewHolder viewHolder,
                                              @NonNull RecyclerView.ViewHolder target) {

                            int pos_dragged = viewHolder.getAdapterPosition();
                            int pos_target = target.getAdapterPosition();
                            Collections.swap(courseListNames, pos_dragged, pos_target);
                            recyclerView.getAdapter().notifyItemMoved(pos_dragged, pos_target);
                            return false;
                        }

                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                             int direction) {
                            //DONOTHING
                        }
                    });
                    helper.attachToRecyclerView(recyclerView);

                    adapter.setOnItemCLickListener(new CourseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(context,
                                    AddClassesDetailsActivity.class);

                            intent.putExtra("index", position);
                            context.startActivity(intent);
                        }
                    });

                } catch (JSONException e){
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

    public void advisorPostCourse(Context context, Course course, String url, int stuId) {
        JSONObject json = null;
        url = url + "/" + stuId;


        try {
            // Convert object to JSON Object
            json = new JSONObject(new Gson().toJson(course));
            json.put("startDate", course.getStartDate());
            json.put("endDate", course.getEndDate());
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
                        System.out.println("successful parsed");
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
}

package com.example.as1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.Adapters.CourseAdapter;
import com.example.as1.Controller.CourseController;
import com.example.as1.Models.Course;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassDragDropActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Course> courseList = new ArrayList<Course>();
    ArrayList<String> namesList = new ArrayList<String>();
    Button nextBtn, prevBtn;
    CourseAdapter adapter;
    CourseController controller = new CourseController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_drag_drop);
        recyclerView = findViewById(R.id.recycler_drag_drop);
        nextBtn = findViewById(R.id.drag_drop_next_btn);
        prevBtn = findViewById(R.id.drag_drop_prev_btn);
        User user = User.getUser(ClassDragDropActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        controller.sendGetCourseListRequest(ClassDragDropActivity.this, URL.URL_GET_COURSES, user.getId(), recyclerView);




        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.sendPostRequestCourseList(ClassDragDropActivity.this, courseList,
                        URL.URL_POST_ORDERED_COURSES);

                user.setCoursesList(courseList);
                User.saveUser(ClassDragDropActivity.this, user);

                Intent intent;
                Intent fromIntent = getIntent();

                if (fromIntent.getStringExtra("activity").equals("fromSummary")) {
                    intent = new Intent(ClassDragDropActivity.this,
                            JourneySummaryActivity.class);
                }
                else {
                    intent = new Intent(ClassDragDropActivity.this,
                            StudyHoursActivity.class);
                }

                startActivity(intent);
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassDragDropActivity.this,
                        AddClassesActivity.class);

                intent.putExtra("activity", "fromDrag");
                startActivity(intent);
            }
        });
    }

    public void sendGetRequestCourseList(Context context, String url, TextView textview) {
        RequestQueue queue;
        queue = Volley.newRequestQueue(context);
        User user = User.getUser(context);
        String id = String.valueOf(user.getId());
        url = url + "/" + id;


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        System.out.println(response.toString());
                        if (textview != null) {
                            textview.setText(response.toString());
                        }


                        try {
                            JSONArray jsonArray = response.getJSONArray("courses");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject singleCourse = jsonArray.getJSONObject(i);
                                Course nonJSONCourse = new Course();

                                //Initialize each variable to pass the object
                                String course = singleCourse.getString("course");
                                int credits = singleCourse.getInt("credits");
                                String days = singleCourse.getString("days");
                                double endTime = singleCourse.getDouble("endTime");
                                //boolean lab = singleCourse.getBoolean("lab");
                                String location = singleCourse.getString("location");
                                double startTime = singleCourse.getDouble("startTime");
                                double time = singleCourse.getDouble("time");
                                int importance = singleCourse.getInt("importance");

                                //Set JSON values to the object
                                nonJSONCourse.setCredits(credits);
                                nonJSONCourse.setCourseName(course);
                                nonJSONCourse.setDays(days);
                                nonJSONCourse.setLocation(location);
                                nonJSONCourse.setTime(time);
                                nonJSONCourse.setStartTime(startTime);
                                nonJSONCourse.setImportance(importance);
                                nonJSONCourse.setEndTime(endTime);

                                courseList.add(i, nonJSONCourse);
                            }






                        } catch (JSONException e) {
                            throw new RuntimeException(e);
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
}

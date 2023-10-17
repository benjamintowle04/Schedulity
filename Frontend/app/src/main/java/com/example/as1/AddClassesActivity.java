package com.example.as1;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.as1.Enum.AccountType;
import com.example.as1.Models.Course;
import com.example.as1.Models.CourseList;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddClassesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CourseAdapter adapter;
    CourseList displayedCourses = new CourseList(new ArrayList<Course>());
    List<Course> courseList = new ArrayList<Course>();
    ArrayList<String> recyclerElements = new ArrayList<String>();
    int numItems = 0;
    CourseController courseController = new CourseController();

    Button addBtn, nextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);
        recyclerView = findViewById(R.id.course_list_recyclerview);
        nextButton = findViewById(R.id.next_classes_added_btn);
        addBtn = findViewById(R.id.add_classes_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddClassesActivity.this));


        User user = User.getUser(AddClassesActivity.this);


        Intent intent = getIntent();
        String extra = intent.getStringExtra("from");
        int id = intent.getIntExtra("studentId", Integer.MAX_VALUE);

        if ((extra != null && extra.equals("fromAdvisor") || user.getAccountType() == AccountType.ADVISER)) {
           //Get my students courses
            id = intent.getIntExtra("studentId", Integer.MAX_VALUE);
            courseController.sendGetCourseListRequest(this, URL.URL_GET_COURSES, id, recyclerView);
        }

        else {
            //Get my own courses if i am a student
            courseController.sendGetCourseListRequest(this, URL.URL_GET_COURSES, user.getId(), recyclerView);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.drawable.baseline_create_24);
        getSupportActionBar().setTitle("  Add Classes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        int finalId = id;
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddClassesActivity.this,
                        AddClassesDetailsActivity.class);
                if (user.getAccountType() == AccountType.ADVISER) {
                    intent.putExtra("stuId", finalId);
                }
                startActivity(intent);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (user.getAccountType() == AccountType.ADVISER) {
                     intent = new Intent(AddClassesActivity.this, MainActivity.class);
                }
                else {
                     intent = new Intent(AddClassesActivity.this,
                            ClassDragDropActivity.class);
                }

                Intent fromIntent = getIntent();
                if (fromIntent.getSerializableExtra("activity").equals("fromSummary")) {
                    intent.putExtra("activity", "fromSummary");
                }
                else {
                    intent.putExtra("activity", "notFromSummary");
                }

                startActivity(intent);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String extra = intent.getStringExtra("from");
        int id;
        User user = User.getUser(this);
        if ((extra != null && extra.equals("fromAdvisor") || user.getAccountType() == AccountType.ADVISER)) {
            //Get my students courses
            id = intent.getIntExtra("studentId", Integer.MAX_VALUE);
            courseController.sendGetCourseListRequest(this, URL.URL_GET_COURSES, id, recyclerView);
        }
        else {
            //Get my own courses
            courseController.sendGetCourseListRequest(this, URL.URL_GET_COURSES, user.getId(), recyclerView);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

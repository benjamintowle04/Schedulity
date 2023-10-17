package com.example.as1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.as1.Adapters.CourseAdapter;
import com.example.as1.Controller.CourseController;
import com.example.as1.Enum.AccountType;
import com.example.as1.Models.Course;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

import java.util.ArrayList;
import java.util.List;

public class CourseSummaryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button backBtn, editBtn;
    CourseAdapter adapter;
    List<Course> courseList;
    ArrayList<String> list = new ArrayList<String>();
    TextView courseSummaryHeader;
    CourseController courseController = new CourseController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_summary);
        recyclerView = findViewById(R.id.recycler_course_summary);
        recyclerView.setLayoutManager(new LinearLayoutManager(CourseSummaryActivity.this));
        backBtn = findViewById(R.id.back_btn_course_summary);
        editBtn = findViewById(R.id.edit_btn_course_summary);
        courseSummaryHeader = findViewById(R.id.course_summary_header);
        User user = User.getUser(CourseSummaryActivity.this);
        Intent fromIntent = getIntent();
        int parentOrStudentId = fromIntent.getIntExtra("id", Integer.MAX_VALUE);
        String kidOrStudentName = fromIntent.getStringExtra("friendName");

        boolean for_parent_or_advisor = fromIntent.getStringExtra("from") != null
                && fromIntent.getStringExtra("from").equals("fromViewCourses");

        if (for_parent_or_advisor) {
            courseSummaryHeader.setText("Viewing the Courses of: " + kidOrStudentName);
            if (user.getAccountType() == AccountType.PARENT_USER) {
                editBtn.setVisibility(View.GONE);
                courseSummaryHeader.append("\nView Only");
            }
            courseController.sendGetCourseListRequest(this, URL.URL_GET_COURSES, parentOrStudentId, recyclerView);
        }
        else {
            courseController.sendGetCourseListRequest(this, URL.URL_GET_COURSES, user.getId(), recyclerView);
        }


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseSummaryActivity.this,
                        AddClassesActivity.class);
                if (for_parent_or_advisor) {
                    intent.putExtra("from", "fromAdvisor");
                    intent.putExtra("studentId", parentOrStudentId);
                }
                intent.putExtra("activity", "fromSummary");
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromIntent.getStringExtra("from") != null && fromIntent.getStringExtra("from").equals("fromViewCourses")) {
                    onBackPressed();
                }
                else {
                    startActivity(new Intent(CourseSummaryActivity.this,
                            JourneySummaryActivity.class));
                }
            }
        });



    }
}
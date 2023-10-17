package com.example.as1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.as1.Adapters.CourseAdapter;
import com.example.as1.Controller.ActivityController;
import com.example.as1.Models.Extracurricular;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

import java.util.ArrayList;
import java.util.List;

public class ExtraSummaryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button backBtn, editBtn;
    CourseAdapter adapter;
    List<Extracurricular> activityList;
    ArrayList<String> list = new ArrayList<String>();
    ActivityController controller = new ActivityController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_summary);
        recyclerView = findViewById(R.id.recycler_extra_summary);
        recyclerView.setLayoutManager(new LinearLayoutManager(ExtraSummaryActivity.this));
        backBtn = findViewById(R.id.back_btn_extra_summary);
        editBtn = findViewById(R.id.edit_btn_extra_summary);
        User user = User.getUser(ExtraSummaryActivity.this);
        controller.getActivityList(this, URL.URL_GET_ETRACURRICULAR_LIST, recyclerView);


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExtraSummaryActivity.this,
                        AddExtrasActivity.class);

                intent.putExtra("activity", "fromSummary");
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExtraSummaryActivity.this,
                        JourneySummaryActivity.class));
            }
        });


    }
}
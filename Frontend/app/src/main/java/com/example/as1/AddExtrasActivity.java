package com.example.as1;

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

public class AddExtrasActivity extends AppCompatActivity {
    User user;
    Button addBtn, nextBtn;
    RecyclerView recyclerView;

    ActivityController controller = new ActivityController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_extras);
        addBtn = findViewById(R.id.add_btn_extras);
        nextBtn = findViewById(R.id.next_btn_extras);
        user = User.getUser(AddExtrasActivity.this);


        //Setting up the recyclerView
        recyclerView = findViewById(R.id.recycler_extras);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        controller.getActivityList(this, URL.URL_GET_ETRACURRICULAR_LIST, recyclerView);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddExtrasActivity.this,
                        AddExtrasDetailsActivity.class);

                startActivity(intent);

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddExtrasActivity.this,
                        JourneySummaryActivity.class);

                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        controller.getActivityList(this, URL.URL_GET_ETRACURRICULAR_LIST, recyclerView);
    }

}
package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.as1.Models.Course;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class StudyHoursActivity extends AppCompatActivity {
    RadioButton radio8to10, radio11to13, radio14to16, radio17plus;
    Button nextBtn;
    int studyHours;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_hours);
        User user = User.getUser(StudyHoursActivity.this);

        radio8to10 = findViewById(R.id.radio_8_10);
        radio11to13= findViewById(R.id.radio_11_13);
        radio14to16 = findViewById(R.id.radio_14_16);
        radio17plus = findViewById(R.id.radio_17_plus);
        nextBtn= findViewById(R.id.next_btn_study);
        Intent fromIntent = getIntent();

        //Set the data to the user data if we are editing a previous input
        if (fromIntent.getStringExtra("activity") != null &&
                fromIntent.getStringExtra("activity").equals("fromSummary")) {

            if (user.getStudyHours() == 10) {radio8to10.setChecked(true);}
            if (user.getStudyHours() == 13) {radio11to13.setChecked(true);}
            if (user.getStudyHours() == 16) {radio14to16.setChecked(true);}
            if (user.getStudyHours() == 17) {radio17plus.setChecked(true);}

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.drawable.baseline_school_24);
        getSupportActionBar().setTitle("  Study Hours");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(radio8to10.isChecked()){
                   studyHours = 10;
               } else if (radio11to13.isChecked()) {
                   studyHours = 13;
               } else if (radio14to16.isChecked()) {
                   studyHours = 16;
               } else if (radio17plus.isChecked()) {
                   studyHours = 17;
               }
                Intent intent;
                String url = URL.URL_POST_ORDERED_COURSES;
                sendPostRequestStudyHours(StudyHoursActivity.this, url);
                user.setStudyHours(studyHours);
                User.saveUser(StudyHoursActivity.this, user);

                if (fromIntent.getStringExtra("activity") != null &&
                        fromIntent.getStringExtra("activity").equals("fromSummary")) {

                    intent = new Intent(StudyHoursActivity.this,
                            JourneySummaryActivity.class);
                }
                else {
                    intent = new Intent(StudyHoursActivity.this,
                            SleepyTimeActivity.class);
                }

                startActivity(intent);
            }
        });

    }

    public void sendPostRequestStudyHours(Context context, String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        User user = User.getUser(context);
        String id = String.valueOf(user.getId());
        url = url + "/" + id + "/" + studyHours;


        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response.toString());
                        try {
                            String displayText = "";
                            double studyTime;
                            if (response.length() > user.getCoursesList().size()) {
                                for (int i = 1; i < response.length(); i++) {
                                    JSONObject courseJson = response.getJSONObject(i);
                                    studyTime = Course.fromJsonTime(courseJson);
                                    String name = Course.fromJsonName(courseJson);
                                    DecimalFormat df = new DecimalFormat("0.00");
                                    String f = df.format(studyTime);
                                    user.setCourseTime(studyTime,i-1);
                                    System.out.println(user.getCoursesList().get(i-1).getTime());

                                    displayText += "Study Hours for " + name + " is " + f + " Hours\n";
                                }

                            } else {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject courseJson = response.getJSONObject(i);
                                    studyTime = Course.fromJsonTime(courseJson);
                                    String name = Course.fromJsonName(courseJson);
                                    DecimalFormat df = new DecimalFormat("0.00");
                                    String f = df.format(studyTime);
                                    user.setCourseTime(studyTime, i);
                                    System.out.println(user.getCoursesList().get(i).getTime());

                                    displayText += "Study Hours for " + name + " is " + f + " Hours\n" ;
                                }
                            }

                            Toast toast = Toast.makeText(context, displayText, Toast.LENGTH_LONG);
                            toast.show();
                            User.saveUser(context, user);


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

        queue.add(request);

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
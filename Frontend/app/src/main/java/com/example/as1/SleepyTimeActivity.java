package com.example.as1;


import static com.example.as1.JourneySummaryActivity.convertTo12HourTime;
import static com.example.as1.Utils.TImeConversions.convertTimeToDouble;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.as1.Controller.SleepTimeController;
import com.example.as1.Models.SleepTime;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

public class SleepyTimeActivity extends AppCompatActivity {
    Button nextBtn, prevBtn;
    Switch amPmWeekend, amPmWeeknight;
    EditText weekendTimeU, weeknightTimeU, weekendHoursU, weeknightHoursU;
    double weekendTime, weeknightTime;
    String weekendTimeString, weeknightTimeString;
    int weekendHours, weeknightHours;
    SleepTimeController controller = new SleepTimeController();

    SleepTime sleepTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleepy_time);
        User user = User.getUser(SleepyTimeActivity.this);

        nextBtn = findViewById(R.id.next_btn_sleepy);
        prevBtn = findViewById(R.id.prev_btn_sleepy);
        amPmWeekend = findViewById(R.id.am_pm_weekend);
        amPmWeeknight = findViewById(R.id.am_pm_weeknight);
        weekendTimeU = findViewById(R.id.bedtime_weekend);
        weeknightTimeU = findViewById(R.id.bedtime_weeknight);
        weekendHoursU = findViewById(R.id.weekend_hours_input);
        weeknightHoursU = findViewById(R.id.weeknight_hours_input);


        Intent fromIntent = getIntent();
        if (fromIntent.getStringExtra("activity") != null &&
                fromIntent.getStringExtra("activity").equals("fromSummary")) {

            weeknightHoursU.setText(String.valueOf(user.getSleepTime().getWeeknightHours()));
            weekendHoursU.setText(String.valueOf(user.getSleepTime().getWeekendHours()));

            //Having the time of my life making these time conversions
            String weekend = String.valueOf(user.getSleepTime().getWeekendTime());
            String weeknight = String.valueOf(user.getSleepTime().getWeeknightTime());
            if (weekend.charAt(1) == '.') {weekend = "0" + weekend;}
            if (weeknight.charAt(1) == '.') {weeknight = "0" + weeknight;}
            weekend = weekend.substring(0, 2) + ":" + weekend.substring(3);
            weeknight = weeknight.substring(0, 2) + ":" + weeknight.substring(3);
            if(weekend.length() == 4) {weekend = weekend + "0";}
            if(weeknight.length() == 4) {weeknight = weeknight + "0";}
            weekendTimeU.setText(convertTo12HourTime(weekend).substring(0,5));
            weeknightTimeU.setText(convertTo12HourTime(weeknight).substring(0,5));
            if(convertTo12HourTime(weekend).indexOf('A') != -1) {
                amPmWeeknight.setText("AM");
                amPmWeekend.setChecked(true);
            }
            if(convertTo12HourTime(weeknight).indexOf('A') != -1) {
                amPmWeeknight.setText("AM");
                amPmWeeknight.setChecked(true);
            }

        }


        //Allow User to switch from am to pm using switch buttons
        amPmWeekend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSwitchClick(this);
            }
        });

        amPmWeeknight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSwitchClick(this);
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SleepyTimeActivity.this,
                        ClassDragDropActivity.class));
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weeknightTimeString = weeknightTimeU.getText().toString();
                weekendTimeString = weekendTimeU.getText().toString();

                if (amPmWeekend.isChecked()) {
                    weekendTimeString +="AM";
                } else {
                    weekendTimeString += "PM";
                }

                if (amPmWeeknight.isChecked()) {
                    weeknightTimeString += "AM";
                } else {
                    weeknightTimeString += "PM";
                }

                weeknightTime = convertTimeToDouble(weeknightTimeString);
                weekendTime= convertTimeToDouble(weekendTimeString);
                weekendHours = Integer.parseInt(weekendHoursU.getText().toString());
                weeknightHours = Integer.parseInt(weeknightHoursU.getText().toString());

                sleepTime = new SleepTime(weekendHours, weeknightHours, weekendTime, weeknightTime);
                if (fromIntent.getStringExtra("activity") == null || !fromIntent.getStringExtra("activity").equals("fromSummary")) {
                    controller.postSleepTIme(SleepyTimeActivity.this, sleepTime, URL.URL_POST_SLEEP_TIME);
                }

                user.setSleepTime(sleepTime);
                User.saveUser(SleepyTimeActivity.this, user);
                Intent intent;

                if (fromIntent.getStringExtra("activity") != null &&
                        fromIntent.getStringExtra("activity").equals("fromSummary")) {

                    intent = new Intent(SleepyTimeActivity.this, JourneySummaryActivity.class);
                }

                else {
                    intent = new Intent(SleepyTimeActivity.this, AddExtrasActivity.class);
                }

                startActivity(intent);
            }
        });


    }

    public void onSwitchClick (View.OnClickListener v) {
        if (amPmWeeknight.isChecked()) {
            amPmWeeknight.setText("AM");
        } else {
            amPmWeeknight.setText("PM");
        }


        if (amPmWeekend.isChecked()) {
            amPmWeekend.setText("AM");
        } else {
            amPmWeekend.setText("PM");
        }


    }
}
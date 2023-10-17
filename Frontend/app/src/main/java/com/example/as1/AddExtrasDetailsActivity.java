package com.example.as1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.as1.Controller.ActivityController;
import com.example.as1.Models.Extracurricular;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

import java.util.ArrayList;

public class AddExtrasDetailsActivity extends AppCompatActivity {
    Extracurricular activity;
    EditText nameU, locationU;
    String name;
    double startTime;
    double endTime;
    String startString;
    String endString;
    String location;
    TimePicker timePickerStart, timePickerEnd;
    TextView timeTxtStart, timeTxtEnd;
    Button nextBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_extras_details);
        User user = User.getUser(AddExtrasDetailsActivity.this);
        Intent getIntent = getIntent();
        if (user.getActivityList() == null) {
            user.initializeExtrasList();
        }
        int index = getIntent.getIntExtra("index", user.getActivityList().size());

        nameU = findViewById(R.id.activity_name_input);
        locationU = findViewById(R.id.activity_location_input);
        timePickerStart = findViewById(R.id.time_picker_start);
        timePickerEnd = findViewById(R.id.time_picker_end);
        timeTxtStart = findViewById(R.id.time_display_start);
        timeTxtEnd = findViewById(R.id.time_display_end);
        nextBtn = findViewById(R.id.next_btn_extras_details);

       //If we are editing a current extracurricular activity
        if (index != user.getActivityList().size()){
            nameU.setText(user.getActivityList().get(index).getName());
            locationU.setText(user.getActivityList().get(index).getLocation());
            startTime = user.getActivityList().get(index).getStartTimeDouble();
            endTime = user.getActivityList().get(index).getEndTimeDouble();

           //Tedious time formatting
            String start = String.valueOf(startTime);
            String end = String.valueOf(endTime);
            if (start.charAt(1) == '.') {start = "0" + start;}
            if (end.charAt(1) == '.') {end = "0" + end;}
            start = start.substring(0, 2) + ":" + start.substring(3);
            end = end.substring(0, 2) + ":" + end.substring(3);
            timeTxtStart.setText(start);
            timeTxtEnd.setText(end);

            if (Integer.parseInt(start.substring(0,2)) > 12) {
                timePickerStart.setHour(Integer.parseInt(start.substring(0,2)) - 12);
            }
            else {
                timePickerStart.setHour(Integer.parseInt(start.substring(0,2)));
            }
            timePickerStart.setMinute(Integer.parseInt(start.substring(3)));


            if (Integer.parseInt(end.substring(0,2)) > 12) {
                timePickerEnd.setHour(Integer.parseInt(end.substring(0,2)) - 12);
            }
            else {
                timePickerEnd.setHour(Integer.parseInt(end.substring(0,2)));
            }
            timePickerEnd.setMinute(Integer.parseInt(end.substring(3)));
        }


        timePickerStart.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                if (minute >= 10) {
                    timeTxtStart.setText(hour + ":" + minute);
                }
                else {
                    timeTxtStart.setText(hour + ":0" + minute);
                }
            }
        });

        timePickerEnd.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                if (minute >= 10) {
                    timeTxtEnd.setText(hour + ":" + minute);
                }
                else {
                    timeTxtEnd.setText(hour + ":0" + minute);
                }
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameU.getText().toString();
                location = locationU.getText().toString();

                //Converting input times into the correct double format
                startString = timeTxtStart.getText().toString();
                endString = timeTxtEnd.getText().toString();
                if (startString.charAt(1) == ':') {startString = "0" + startString;}
                if (endString.charAt(1) == ':') {endString = "0" + endString;}
                startString = startString.substring(0,2) + "." + startString.substring(3);
                endString = endString.substring(0,2) + "." + endString.substring(3);

                startTime = Double.parseDouble(startString);
                endTime = Double.parseDouble(endString);

                Intent intent = new Intent(AddExtrasDetailsActivity.this,
                        ExtraMeetingDaysActivity.class);

                if (getIntent.getStringExtra("activity") != null
                        && getIntent.getStringExtra("activity").equals("fromSummary")) {
                    intent.putExtra("activity2", "fromSummary");
                }



                if (index == user.getActivityList().size()) {
                    //Saving everything to the extracurricular object
                    activity = new Extracurricular(name, startTime, endTime, "",
                            location);



                    user.addToActivityList(activity);
                    intent.putExtra("activity", "fromAdd");
                }

                else {
                    String s = user.getActivityList().get(index).getMeetingDays();
                    System.out.println(s);
                    //Saving everything to the extracurricular object
                    activity = new Extracurricular(name, startTime, endTime, s, location);
                    user.getActivityList().set(index, activity);
                    intent.putExtra("activity", "fromEdit");
                }

                User.saveUser(AddExtrasDetailsActivity.this, user);


                intent.putExtra("index", index);
                startActivity(intent);
            }
        });

        //I just want to make a new commit message :) its fun
    }

}

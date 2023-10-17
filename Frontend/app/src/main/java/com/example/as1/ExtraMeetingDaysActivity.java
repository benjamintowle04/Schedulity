package com.example.as1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.as1.Controller.ActivityController;
import com.example.as1.Models.Extracurricular;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

public class ExtraMeetingDaysActivity extends AppCompatActivity {
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    Button prevBtn, submitBtn;
    String days = "";
    ActivityController controller = new ActivityController();
    public boolean isABoxChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_meeting_days);
        User user = User.getUser(ExtraMeetingDaysActivity.this);
        Intent indexIntent = getIntent();

        int index = indexIntent.getIntExtra("index", user.getActivityList().size());
        boolean fromAdd = indexIntent.getStringExtra("activity").equals("fromAdd");

        monday = findViewById(R.id.check_mondays);
        tuesday = findViewById(R.id.check_tuesdays);
        wednesday = findViewById(R.id.check_wednesdays);
        thursday = findViewById(R.id.check_thursdays);
        friday = findViewById(R.id.check_fridays);
        saturday = findViewById(R.id.check_saturdays);
        sunday = findViewById(R.id.check_sundays);
        submitBtn = findViewById(R.id.submit_btn_meeting_days);
        prevBtn = findViewById(R.id.prev_button_meeting_days);

        //If we are changing an existing extracurricular activity
        if (!fromAdd) {
            String currentDays = user.getActivityList().get(index).getMeetingDays();
            if (currentDays.indexOf("M") != -1) {monday.setChecked(true);}
            if (currentDays.indexOf("T") != -1) {tuesday.setChecked(true);}
            if (currentDays.indexOf("W") != -1) {wednesday.setChecked(true);}
            if (currentDays.indexOf("R") != -1) {thursday.setChecked(true);}
            if (currentDays.indexOf("F") != -1) {friday.setChecked(true);}
            if (currentDays.indexOf("S") != -1) {saturday.setChecked(true);}
            if (currentDays.indexOf("U") != -1) {sunday.setChecked(true);}
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (monday.isChecked()){days += "M "; isABoxChecked = true;}
                if (tuesday.isChecked()){days += "T "; isABoxChecked = true;}
                if (wednesday.isChecked()){days += "W "; isABoxChecked = true;}
                if (thursday.isChecked()){days += "R "; isABoxChecked = true;}
                if (friday.isChecked()){days += "F "; isABoxChecked = true;}
                if (saturday.isChecked()){days += "S "; isABoxChecked = true;}
                if (sunday.isChecked()){days += "U "; isABoxChecked = true;}

                if (isABoxChecked) {
                    Extracurricular activity = user.getActivityList().get(index);
                    user.getActivityList().get(index).setMeetingDays(days);

                    controller.postActivity(ExtraMeetingDaysActivity.this,
                            URL.URL_POST_EXTRACURRICULAR, activity);

                    User.saveUser(ExtraMeetingDaysActivity.this, user);

                    Intent intent;
                    if (indexIntent.getStringExtra("activity2") != null &&
                            indexIntent.getStringExtra("activity2").equals("fromSummary")) {

                        intent = new Intent(ExtraMeetingDaysActivity.this,
                                ExtraSummaryActivity.class);
                    } else {
                        intent = new Intent(ExtraMeetingDaysActivity.this,
                                AddExtrasActivity.class);
                    }
                    startActivity(intent);
                }
                else {
                    Toast toast = Toast.makeText(ExtraMeetingDaysActivity.this, "check at leas one box", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });


        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExtraMeetingDaysActivity.this,
                        AddExtrasDetailsActivity.class);
                intent.putExtra("index", index);

                startActivity(intent);
            }
        });

    }
}
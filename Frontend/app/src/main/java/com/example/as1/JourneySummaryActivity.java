package com.example.as1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.as1.Models.User;

public class JourneySummaryActivity extends AppCompatActivity {
    TextView gymData, sleepyDataWeekends, sleepyDataWeeknights, studyData;
    Button editGym, viewCourses, editStudy, viewExtras, editSleepy, confirmBtn;

    String weekendTimeString, weeknightTimeString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_summary);

        gymData = findViewById(R.id.gym_data);
        sleepyDataWeekends = findViewById(R.id.sleepy_data_weekends);
        sleepyDataWeeknights = findViewById(R.id.sleepy_data_weeknights);
        studyData = findViewById(R.id.study_hours_data);
        editGym = findViewById(R.id.edit_btn_gym);
        viewCourses = findViewById(R.id.click_to_view_courses);
        editStudy = findViewById(R.id.edit_btn_study);
        viewExtras = findViewById(R.id.click_to_view_extras);
        editSleepy = findViewById(R.id.edit_btn_sleepy);
        confirmBtn = findViewById(R.id.confirm_btn_summary);
        User user = User.getUser(JourneySummaryActivity.this);

        gymData.setText("I work out " + user.getGymDays() + " days a week, "
                + user.getMinutesPerWorkout() + " minutes per day");

        if (user.getStudyHours() != 17) {
            studyData.setText("I study for " + (user.getStudyHours() - 2) + " to " + user.getStudyHours() +
                    " hours per week");
        }
        else {
            studyData.setText("I study for " + user.getStudyHours() + " or more hours per week");
        }

        //More tedious time converting
        weekendTimeString = String.valueOf(user.getSleepTime().getWeekendTime());
        weeknightTimeString = String.valueOf(user.getSleepTime().getWeeknightTime());
        if (weekendTimeString.charAt(1) == '.') {weekendTimeString = "0" + weekendTimeString;}
        if (weeknightTimeString.charAt(1) == '.') {weeknightTimeString = "0" + weeknightTimeString;}
        weeknightTimeString = weeknightTimeString.substring(0,2) + ":" + weeknightTimeString.substring(3);
        weekendTimeString = weekendTimeString.substring(0,2) + ":" + weekendTimeString.substring(3);

        sleepyDataWeeknights.setText("I sleep " + user.getSleepTime().getWeeknightHours() +
                " hours on weeknights with a bed time of " + convertTo12HourTime(weeknightTimeString));

        sleepyDataWeekends.setText("I sleep " + user.getSleepTime().getWeekendHours() +
                " hours on weekends with a bed time of " + convertTo12HourTime(weekendTimeString));


        //Setting up all button functionality
        viewCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JourneySummaryActivity.this,
                        CourseSummaryActivity.class);

                startActivity(intent);
            }
        });

        viewExtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JourneySummaryActivity.this,
                        ExtraSummaryActivity.class);

                startActivity(intent);
            }
        });

        editGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (JourneySummaryActivity.this,
                        GymHoursActivity.class);

                intent.putExtra("activity", "fromSummary");
                startActivity(intent);

            }
        });

        editStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (JourneySummaryActivity.this,
                        StudyHoursActivity.class);

                intent.putExtra("activity", "fromSummary");
                startActivity(intent);
            }
        });

        editSleepy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (JourneySummaryActivity.this,
                        SleepyTimeActivity.class);

                intent.putExtra("activity", "fromSummary");
                startActivity(intent);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (JourneySummaryActivity.this,
                        MainActivity.class);

                startActivity(intent);
            }
        });




    }

    public static String convertTo12HourTime(String militaryTime) {
        String[] timeComponents = militaryTime.split(":");
        int hours = Integer.parseInt(timeComponents[0]);
        int minutes = Integer.parseInt(timeComponents[1]);
        String period = "AM";

        if (hours == 0) {
            hours = 12;
        } else if (hours >= 12) {
            period = "PM";
            if (hours > 12) {
                hours = hours - 12;
            }
        }

        return String.format("%d:%02d %s", hours, minutes, period);
    }
}
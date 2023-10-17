package com.example.as1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.as1.Models.User;

public class GymHoursActivity extends AppCompatActivity {
    RadioButton radio0, radio1, radio2, radio3, radio4, radio5, radio6, radio7;
    Button nextBtn, prevBtn;
    String minutesInput;
    int dayCount = 0;
    EditText minutesPerDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_hours);
        Intent fromIntent = getIntent();
        User user = User.getUser(GymHoursActivity.this);

        //Set our buttons/TextFields
        radio0 = findViewById(R.id.radio_0_days);
        radio1 = findViewById(R.id.radio_1_day);
        radio2 = findViewById(R.id.radio_2_days);
        radio3 = findViewById(R.id.radio_3_days);
        radio4 = findViewById(R.id.radio_4_days);
        radio5 = findViewById(R.id.radio_5_days);
        radio6 = findViewById(R.id.radio_6_days);
        radio7 = findViewById(R.id.radio_7_days);
        nextBtn = findViewById(R.id.next_btn_gym);
        prevBtn = findViewById(R.id.prev_btn_gym);
        minutesPerDay = findViewById(R.id.hours_per_day);

        //Set the data to the user data if we are editing a previous input
        if (fromIntent.getStringExtra("activity") != null &&
                fromIntent.getStringExtra("activity").equals("fromSummary")) {

            minutesPerDay.setText(String.valueOf(user.getMinutesPerWorkout()));
            if (user.getGymDays() == 0) {radio0.setChecked(true);}
            else if (user.getGymDays() == 1) {radio1.setChecked(true);}
            else if (user.getGymDays() == 2) {radio2.setChecked(true);}
            else if (user.getGymDays() == 3) {radio3.setChecked(true);}
            else if (user.getGymDays() == 4) {radio4.setChecked(true);}
            else if (user.getGymDays() == 5) {radio5.setChecked(true);}
            else if (user.getGymDays() == 6) {radio6.setChecked(true);}
            else if (user.getGymDays() == 7) {radio7.setChecked(true);}

        }



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.drawable.baseline_fitness_center_24);
        getSupportActionBar().setTitle("  Add Gym Hours");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GymHoursActivity.this, StudyHoursActivity.class);
                startActivity(intent);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check which radioButton is pushed (if none, then dayCount is 0)
                if(radio1.isChecked()) {dayCount = 1;}
                if(radio2.isChecked()) {dayCount = 2;}
                if(radio3.isChecked()) {dayCount = 3;}
                if(radio4.isChecked()) {dayCount = 4;}
                if(radio5.isChecked()) {dayCount = 5;}
                if(radio6.isChecked()) {dayCount = 6;}
                if(radio7.isChecked()) {dayCount = 7;}

                user.setGymDays(dayCount);
                minutesInput = minutesPerDay.getText().toString();

                if (!TextUtils.isEmpty(minutesInput) ) {
                    user.setMinutesPerWorkout(Integer.parseInt(minutesInput));
                    user.saveUser(GymHoursActivity.this);

                    System.out.println(user.getGymDays());
                    System.out.println(user.getMinutesPerWorkout());

                    Intent intent;

                    if (fromIntent.getStringExtra("activity") != null &&
                            fromIntent.getStringExtra("activity").equals("fromSummary")) {

                        intent = new Intent(GymHoursActivity.this,
                                JourneySummaryActivity.class);
                    }
                    else {
                        intent = new Intent(GymHoursActivity.this,
                                AddClassesActivity.class);
                        intent.putExtra("activity", "fromGym");
                    }

                    startActivity(intent);
                }
                else {
                    Toast toast = Toast.makeText(GymHoursActivity.this,
                            "Please fill in Missing Field", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });






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
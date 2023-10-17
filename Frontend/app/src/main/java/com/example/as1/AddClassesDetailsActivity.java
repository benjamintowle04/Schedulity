package com.example.as1;

import static com.example.as1.JourneySummaryActivity.convertTo12HourTime;
import static com.example.as1.Utils.TImeConversions.convertTimeToDouble;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.as1.Controller.CourseController;
import com.example.as1.Enum.AccountType;
import com.example.as1.Models.Course;
import com.example.as1.Models.User;
import com.example.as1.Utils.URL;

import java.util.ArrayList;


public class AddClassesDetailsActivity extends AppCompatActivity {
    //Variables used for assigning posted object variables
    Switch amPmStart;
    Switch amPmEnd;
    Button addBtn;
    EditText courseName_u;
    EditText courseLocation_u;
    EditText startTime_u;
    EditText endTime_u;
    EditText credits_u;
    RadioButton hasLab_u;
    RadioButton hasNoLab_u;
    ArrayList<CheckBox> boxes = new ArrayList<CheckBox>();
    Course userInput;
    CourseController controller = new CourseController();



    //Variables to be posted
    String courseName;
    String courseLocation;
    String startTimeString;
    String endTimeString;
    double startTimeDouble;
    double endTimeDouble;
    String courseDays = "";
    int credits;
    boolean hasLab = false;
    String startDate, endDate;
    int boxesChecked = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes_details);
        int index = 0;
        Intent fromIntent = getIntent();
        User user = User.getUser(AddClassesDetailsActivity.this);
        int stuId = user.getId();

        if (user.getCoursesList() != null) {
            index = fromIntent.getIntExtra("index", user.getCoursesList().size());
        }

        //assign all input variables
        amPmStart = findViewById(R.id.am_pm_start);
        amPmEnd =  findViewById(R.id.am_pm_end);
        addBtn = findViewById(R.id.add_course_details);
        courseName_u = findViewById(R.id.course_name_input);
        courseLocation_u = findViewById(R.id.course_location_input);
        startTime_u = findViewById(R.id.start_time_input);
        endTime_u = findViewById(R.id.end_time_input);
        credits_u = findViewById(R.id.credits_input);
        hasLab_u = findViewById(R.id.radio_fall);
        hasNoLab_u = findViewById(R.id.radio_spring);

        //initialize the checkboxes (NOTE: first index is SUNDAY not monday
        boxes.add(findViewById(R.id.check_sunday));
        boxes.add(findViewById(R.id.check_monday));
        boxes.add(findViewById(R.id.check_tuesday));
        boxes.add(findViewById(R.id.check_wednesday));
        boxes.add(findViewById(R.id.check_thursday));
        boxes.add(findViewById(R.id.check_friday));
        boxes.add(findViewById(R.id.check_saturday));

        //If we are editing an already added course, we load the previously input values into screen
        if (user.getCoursesList() != null) {
            if (index != user.getCoursesList().size()) {
                courseName_u.setText(user.getCoursesList().get(index).getCourseName());
                courseLocation_u.setText(user.getCoursesList().get(index).getLocation());
                credits_u.setText(String.valueOf(user.getCoursesList().get(index).getCredits()));

                //I love time converting
                String start = String.valueOf(user.getCoursesList().get(index).getStartTimeDouble());
                String end = String.valueOf(user.getCoursesList().get(index).getEndTimeDouble());
                if (start.charAt(1) == '.') {
                    start = "0" + start;
                }
                if (end.charAt(1) == '.') {
                    end = "0" + end;
                }
                start = start.substring(0, 2) + ":" + start.substring(3);
                end = end.substring(0, 2) + ":" + end.substring(3);
                if (start.length() == 4) {
                    start = start + "0";
                }
                if (end.length() == 4) {
                    end = end + "0";
                }
                startTime_u.setText(convertTo12HourTime(start).substring(0, 5));
                endTime_u.setText(convertTo12HourTime(end).substring(0, 5));

                if (convertTo12HourTime(start).indexOf('P') != -1) {
                    amPmStart.setText("PM");
                    amPmStart.setChecked(true);
                }
                if (convertTo12HourTime(end).indexOf('P') != -1) {
                    amPmEnd.setText("PM");
                    amPmEnd.setChecked(true);
                }

                if (user.getCoursesList().get(index).getDays().indexOf('U') != -1) {
                    boxes.get(0).setChecked(true);
                }
                if (user.getCoursesList().get(index).getDays().indexOf('M') != -1) {
                    boxes.get(1).setChecked(true);
                }
                if (user.getCoursesList().get(index).getDays().indexOf('T') != -1) {
                    boxes.get(2).setChecked(true);
                }
                if (user.getCoursesList().get(index).getDays().indexOf('W') != -1) {
                    boxes.get(3).setChecked(true);
                }
                if (user.getCoursesList().get(index).getDays().indexOf('R') != -1) {
                    boxes.get(4).setChecked(true);
                }
                if (user.getCoursesList().get(index).getDays().indexOf('F') != -1) {
                    boxes.get(5).setChecked(true);
                }
                if (user.getCoursesList().get(index).getDays().indexOf('S') != -1) {
                    boxes.get(6).setChecked(true);
                }
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.drawable.baseline_create_24);
        getSupportActionBar().setTitle("  Add Classes Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Allow User to switch from am to pm using switch buttons
        amPmStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSwitchClick(this);
            }
        });

        amPmEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSwitchClick(this);
            }
        });

        int newIndex = index;
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set each String variable to its editText field
                if (courseName_u.getText() == null
                        || courseLocation_u.getText() == null
                        || startTime_u.getText() == null
                        || boxesChecked == 0
                        || endTime_u.getText() == null
                        || credits_u.getText() == null) {
                    courseName = courseName_u.getText().toString();
                    courseLocation = courseLocation_u.getText().toString();
                    startTimeString = startTime_u.getText().toString();
                    endTimeString = endTime_u.getText().toString();
                    credits = Integer.parseInt(credits_u.getText().toString());
                }
                else {
                   Toast toast = Toast.makeText(AddClassesDetailsActivity.this, "fill in all fields", Toast.LENGTH_LONG);
                   toast.show();
                }
                Intent fromIntent = getIntent();
                int id = fromIntent.getIntExtra("stuId", Integer.MAX_VALUE);

                //Simple logic for assigning boolean lab
                if (hasLab_u.isChecked()) {
                    startDate = "8-25-23";
                    endDate = "12-15-23";
                } else if (hasNoLab_u.isChecked()) {
                    startDate = "1-16-23";
                    endDate = "5-12-23";
                }

                //Assigning the course days string variable
                if (boxes.get(0).isChecked()) {
                    courseDays += "U ";
                    boxesChecked = 1;
                }
                if (boxes.get(1).isChecked()) {
                    courseDays += "M ";
                    boxesChecked = 1;
                }
                if (boxes.get(2).isChecked()) {
                    courseDays += "T ";
                    boxesChecked = 1;
                }
                if (boxes.get(3).isChecked()) {
                    courseDays += "W ";
                    boxesChecked = 1;
                }
                if (boxes.get(4).isChecked()) {
                    courseDays += "R ";
                    boxesChecked = 1;
                }
                if (boxes.get(5).isChecked()) {
                    courseDays += "F ";
                    boxesChecked = 1;
                }
                if (boxes.get(6).isChecked()) {
                    courseDays += "S ";
                    boxesChecked = 1;
                }


                //Test for empty fields
                if (courseName_u.getText() == null
                        || courseLocation_u.getText() == null
                        || startTime_u.getText() == null
                        || boxesChecked == 0
                        || endTime_u.getText() == null
                        || credits_u.getText() == null
                        || startDate == null
                        || endDate == null) {

                    Toast toast = Toast.makeText(AddClassesDetailsActivity.this, "Fill in all fields", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    //Add either "AM" or "PM" to the time string
                    if (amPmStart.isChecked()) {
                        startTimeString += "PM";
                    } else {
                        startTimeString += "AM";
                    }

                    if (amPmEnd.isChecked()) {
                        endTimeString += "PM";
                    } else {
                        endTimeString += "AM";
                    }
                    //Convert to military time doubleValue
                    startTimeDouble = convertTimeToDouble(startTimeString);
                    endTimeDouble = convertTimeToDouble(endTimeString);
                    userInput = new Course(startTimeDouble, endTimeDouble, courseName, credits,
                            courseLocation, courseDays);
                    userInput.setStartDate(startDate);
                    userInput.setEndDate(endDate);

                    if (user.getAccountType() == AccountType.ADVISER) {
                        controller.advisorPostCourse(AddClassesDetailsActivity.this,
                                userInput, URL.URL_POST_COURSE, id);
                    } else if (user.getAccountType() == AccountType.NORMAL_USER) {
                        if (user.getCoursesList() != null && newIndex != user.getCoursesList().size()) {
                            user.getCoursesList().set(newIndex, userInput);
                        } else {
                            user.addCourseToList(userInput);
                            controller.sendPostRequestCourse(AddClassesDetailsActivity.this,
                                    userInput, URL.URL_POST_COURSE, startDate, endDate);
                        }

                        User.saveUser(AddClassesDetailsActivity.this, user);
                    }

                    //Logic for directing the next activity (multiple uses for this screen)
                    Intent intent;
                    if (fromIntent.getStringExtra("activity") != null
                            && fromIntent.getStringExtra("activity").equals("fromDragDrop")) {

                        intent = new Intent(AddClassesDetailsActivity.this,
                                ClassDragDropActivity.class);
                    } else if (fromIntent.getStringExtra("activity") != null
                            && fromIntent.getStringExtra("activity").equals("fromSummary")) {

                        intent = new Intent(AddClassesDetailsActivity.this,
                                CourseSummaryActivity.class);
                    } else {
                        intent = new Intent(AddClassesDetailsActivity.this,
                                AddClassesActivity.class);
                    }

                    intent.putExtra("activity", "fromAdd");
                    if (user.getAccountType() == AccountType.ADVISER) {
                        intent.putExtra("from", "fromAdvisor");
                        intent.putExtra("studentId", id);
                    }
                    startActivity(intent);
                }
            }
        });
    }


    public void onSwitchClick (View.OnClickListener v) {
        if (amPmStart.isChecked()) {
            amPmStart.setText("PM");
        } else {
           amPmStart.setText("AM");
        }


        if (amPmEnd.isChecked()) {
            amPmEnd.setText("PM");
        } else {
            amPmEnd.setText("AM");
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
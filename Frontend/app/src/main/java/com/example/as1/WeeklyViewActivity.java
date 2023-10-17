package com.example.as1;

import static com.example.as1.Utils.CalendarUtils.daysInWeekArray;
import static com.example.as1.Utils.CalendarUtils.monthYearFromDate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.as1.Adapters.AppointmentAdapter;
import com.example.as1.Adapters.CalendarAdapter;
import com.example.as1.Enum.LoggedInStates;
import com.example.as1.Enum.ViewType;
import com.example.as1.Models.Appointment;
import com.example.as1.Models.User;
import com.example.as1.Utils.CalendarUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeeklyViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    Button createAppt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_view);


        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        createAppt = findViewById(R.id.createAppointment);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id) {

                    case R.id.nav_home:
                        Intent intent_home = new Intent(WeeklyViewActivity.this, MainActivity.class);
                        startActivity(intent_home);
                        break;
                    case R.id.nav_user:
                        Intent intent_user = new Intent(WeeklyViewActivity.this, UserActivity.class);
                        startActivity(intent_user);
                        break;
                    case R.id.journey:
                        Intent intent_journey = new Intent(WeeklyViewActivity.this, StudyHoursActivity.class);
                        startActivity(intent_journey);
                        break;
                    case R.id.friends:
                        Intent intent_friends = new Intent(WeeklyViewActivity.this, FriendsActivity.class);
                        startActivity(intent_friends);
                        break;
                    case R.id.settings:
                        Toast.makeText(WeeklyViewActivity.this, "Settings is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_enterSchedule:
                        Intent intent_login = new Intent(WeeklyViewActivity.this, LoginActivity.class);
                        startActivity(intent_login);
                        break;
                    case R.id.nav_logout:
                        Toast.makeText(WeeklyViewActivity.this, "Logout is clicked", Toast.LENGTH_SHORT).show();
                        User.removeUser(WeeklyViewActivity.this);
                        Intent intent_logout = new Intent(WeeklyViewActivity.this, LoginActivity.class);
                        startActivity(intent_logout);
                        break;
                    case R.id.help:
                        Toast.makeText(WeeklyViewActivity.this, "Help is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_share:
                        Toast.makeText(WeeklyViewActivity.this, "Share is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_rate:
                        Toast.makeText(WeeklyViewActivity.this, "Rate us is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;

                }
                return true;
            }
        });
        toolbar.setOnMenuItemClickListener(new MaterialToolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                User user = User.getUser(WeeklyViewActivity.this);
                switch (item.getItemId()) {
                    case R.id.MonthlyView:
                        Toast.makeText(WeeklyViewActivity.this, "Monthly View is clicked", Toast.LENGTH_SHORT).show();
                        user.viewType = ViewType.Monthly;
                        Intent intentM = new Intent(WeeklyViewActivity.this, MainActivity.class);
                        startActivity(intentM);
                        break;
                    case R.id.WeeklyView:
                        Toast.makeText(WeeklyViewActivity.this, "Weekly View is clicked", Toast.LENGTH_SHORT).show();
                        user.viewType = ViewType.Weekly;
                        Intent intentW = new Intent(WeeklyViewActivity.this, WeeklyViewActivity.class);
                        startActivity(intentW);
                        break;
                    case R.id.DailyView:
                        Toast.makeText(WeeklyViewActivity.this, "Daily View is clicked", Toast.LENGTH_SHORT).show();
                        user.viewType = ViewType.Daily;
                        Intent intentD = new Intent(WeeklyViewActivity.this, DailyViewActivity.class);
                        startActivity(intentD);
                        break;
                    default:
                        return false;
                }
                user.saveUser(WeeklyViewActivity.this);
                return true;
            }
        });

        initWidgets();
        setWeekView();
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }


    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    public void onItemLongClick(int position, LocalDate date) {
        startActivity(new Intent(this, AppointmentEditActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = User.getUser(WeeklyViewActivity.this);

        //if not logged in intent to
        if (user == null || user.getType() == LoggedInStates.NOT_LOGGED_IN) {
            Intent intent = new Intent(WeeklyViewActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        setEventAdapter();
    }

    private void setEventAdapter() {
        User user = User.getUser(WeeklyViewActivity.this);
        ArrayList<Appointment> dailyAppointments = Appointment.appointmentsForDate(user.appointmentList, CalendarUtils.selectedDate);
        AppointmentAdapter appointmentAdapter;

        if (dailyAppointments == null || dailyAppointments.size() == 0) {
            // Erstellen Sie einen leeren Adapter, wenn keine Termine vorhanden sind
            appointmentAdapter = new AppointmentAdapter(getApplicationContext(), new ArrayList<>());
        } else {
            appointmentAdapter = new AppointmentAdapter(getApplicationContext(), dailyAppointments);
        }

        eventListView.setAdapter(appointmentAdapter);
        setupEventListViewClickListener();
    }

    public void newEventAction(View view) {
        startActivity(new Intent(this, AppointmentEditActivity.class));
    }

    private void setupEventListViewClickListener() {
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Appointment selectedAppointment = (Appointment) parent.getItemAtPosition(position);
                Intent intent = new Intent(WeeklyViewActivity.this, AppointmentEditActivity.class);
                intent.putExtra("appointment", selectedAppointment);
                startActivity(intent);
            }
        });
    }

}
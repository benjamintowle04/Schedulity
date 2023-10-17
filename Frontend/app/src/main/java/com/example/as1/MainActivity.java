package com.example.as1;

import static com.example.as1.Utils.CalendarUtils.daysInMonthArray;
import static com.example.as1.Utils.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.as1.Adapters.CalendarAdapter;
import com.example.as1.Controller.AdvisorController;
import com.example.as1.Controller.AppointmentController;
import com.example.as1.Controller.TempCoursesController;
import com.example.as1.Enum.AccountType;
import com.example.as1.Enum.LoggedInStates;
import com.example.as1.Enum.ViewType;
import com.example.as1.Models.User;
import com.example.as1.Utils.CalendarUtils;
import com.example.as1.Utils.URL;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private WebSocketClient cc;
    AdvisorController advisorController = new AdvisorController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = User.getUser(MainActivity.this);
        NavigationView navigationView;
        int id = user.getId();
        //Different gui for different account types
        if (user != null && user.getAccountType() == AccountType.PARENT_USER) {
            setContentView(R.layout.activity_main_parent);
            navigationView = findViewById(R.id.navigation_view);
            TextView type = navigationView.getHeaderView(0).findViewById(R.id.following_txt);
            type.setText("Parent");
        } else if (user.getAccountType() == AccountType.ADVISER) {
            setContentView(R.layout.activity_main_advisor);
            navigationView = findViewById(R.id.navigation_view);
            TextView type = navigationView.getHeaderView(0).findViewById(R.id.following_txt);
            type.setText("Advisor");
        } else {
            setContentView(R.layout.activity_main);
            navigationView = findViewById(R.id.navigation_view);
            TextView type = navigationView.getHeaderView(0).findViewById(R.id.following_txt);
            type.setText("Student");
        }
        System.out.println(user.getId().toString());
        TextView userNameNav = navigationView.getHeaderView(0).findViewById(R.id.name);
        userNameNav.setText(user.getUsername());

        if(user.getAccountType() == AccountType.ADVISER) {
            advisorController.getAdvisorStudents(MainActivity.this, URL.URL_GET_ADVISOR_INFO, user.getId());
        }


        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
//        setSupportActionBar(toolbar); // Set the toolbar as the support action bar

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        //In case were in a funny little situation
        if ((user.getFriendsList() != null && user.getFriendsList().size() > 0 && User.selectedFriend == null)
                && user.getAccountType() != AccountType.NORMAL_USER) {

            User.selectedFriend = user.getFriendsList().get(user.getFriendsList().size() - 1);
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);

                switch (id) {
                    case R.id.nav_home:
                        Intent intent_home = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent_home);
                        break;
                    case R.id.nav_user:
                        Intent intent_user = new Intent(MainActivity.this, UserActivity.class);
                        startActivity(intent_user);
                        break;
                    case R.id.journey:
                        Intent intent_journey = new Intent(MainActivity.this, GymHoursActivity.class);
                        intent_journey.putExtra("activity", "fromMain");
                        startActivity(intent_journey);
                        break;
                    case R.id.friends:
                        Intent intent_friends = new Intent(MainActivity.this, FriendsActivity.class);
                        startActivity(intent_friends);
                        break;
                    case R.id.settings:
                        Toast.makeText(MainActivity.this, "Settings is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_enterSchedule:
                        Toast.makeText(MainActivity.this, "Import Schedule is clicked", Toast.LENGTH_SHORT).show();
                        Intent intent_importSchedule = new Intent(MainActivity.this, ImportScheduleActivity.class);
                        startActivity(intent_importSchedule);
                        break;
                    case R.id.nav_logout:
                        Toast.makeText(MainActivity.this, "Logout is clicked", Toast.LENGTH_SHORT).show();
                        User.removeUser(MainActivity.this);
                        Intent intent_logout = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent_logout);
                        break;
                    case R.id.help:
                        Toast.makeText(MainActivity.this, "Help is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_share:
                        Toast.makeText(MainActivity.this, "Share is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_rate:
                        Toast.makeText(MainActivity.this, "Rate us is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.add_kids:
                        Intent intentKids = new Intent(MainActivity.this, FriendsActivity.class);
                        startActivity(intentKids);
                        break;
                    case R.id.view_kids_summary:
                        Intent i = new Intent(MainActivity.this, FriendsActivity.class);
                        i.putExtra("from", "fromViewCourses");
                        startActivity(i);
                        break;
                    case R.id.add_students:
                        Intent intentStudents = new Intent(MainActivity.this, FriendsActivity.class);
                        startActivity(intentStudents);
                        break;
                    case R.id.view_students_schedules:
                        Intent in = new Intent(MainActivity.this, FriendsActivity.class);
                        in.putExtra("from", "fromViewCourses");
                        startActivity(in);
                        break;
                    case R.id.put_up_for_adoption:
                        Intent intent = new Intent(MainActivity.this, DisownActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.parent_requests:
                        Intent pIntent = new Intent(MainActivity.this, ViewParentRequestsActivity.class);
                        startActivity(pIntent);
                        break;
                    case R.id.my_parents:
                        Intent intent1 = new Intent(MainActivity.this, ViewParentActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.my_advisor:
                        Intent intent2 = new Intent(MainActivity.this, ViewAdvisorActivity.class);
                        startActivity(intent2);
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
                User user = User.getUser(MainActivity.this);
                switch (item.getItemId()) {
                    case R.id.MonthlyView:
                        Toast.makeText(MainActivity.this, "Monthly View is clicked", Toast.LENGTH_SHORT).show();
                        user.viewType = ViewType.Monthly;
                        Intent intentM = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intentM);
                        break;
                    case R.id.WeeklyView:
                        Toast.makeText(MainActivity.this, "Weekly View is clicked", Toast.LENGTH_SHORT).show();
                        user.viewType = ViewType.Weekly;
                        user.viewType = ViewType.Weekly;
                        Intent intentW = new Intent(MainActivity.this, WeeklyViewActivity.class);
                        startActivity(intentW);

                        break;
                    case R.id.DailyView:
                        Toast.makeText(MainActivity.this, "Daily View is clicked", Toast.LENGTH_SHORT).show();
                        user.viewType = ViewType.Daily;
                        user.viewType = ViewType.Daily;
                        Intent intentD = new Intent(MainActivity.this, DailyViewActivity.class);
                        startActivity(intentD);

                        break;
                    default:
                        return false;
                }
                user.saveUser(MainActivity.this);
                return true;
            }
        });

        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        //setMonthView();

        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray();

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        //Prompts the user to add a student or kid if their dependents/students are empty
        if (!((user.getAccountType() == AccountType.PARENT_USER && (user.getFriendsList() == null || user.getFriendsList().size() == 0))
                || user.getAccountType() == AccountType.ADVISER && (user.getFriendsList() == null || user.getFriendsList().size() == 0))) {

            calendarRecyclerView.setVisibility(View.VISIBLE);
            setMonthView();
            if (User.selectedFriend != null && user.getAccountType() != AccountType.NORMAL_USER) {
                toolbar.setTitle("Schedule of: " + User.selectedFriend.getUsername());
            }
        } else {
            calendarRecyclerView.setVisibility(View.INVISIBLE);
            toolbar.setTitle("Schedulity");
        }
        calendarAdapter.setOnItemCLickListener(new CalendarAdapter.OnItemListener() {
            @Override
            public void onItemClick(int position, LocalDate date) {
                Intent intent = new Intent(MainActivity.this, DailyViewActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position, LocalDate date) {
                String message = "LongPress on Button clicked!!";
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", message);
                startActivity(new Intent(MainActivity.this, AppointmentEditActivity.class));
            }
        });


        setupWebSocketConnection();

        if (User.getUser(MainActivity.this).getAccountType() == AccountType.NORMAL_USER) {
            checkForNewAppointments();
            checkForCourses();
            checkForStudyTime();
        }

    }

    private void checkForStudyTime() {
        TempCoursesController controller = new TempCoursesController();
        String url = URL.URL_GET_STUDYTIME.replace("{id}", User.getUser(MainActivity.this).getId().toString());
        controller.getTempCourses(MainActivity.this, url);
    }

    private void checkForCourses() {
        TempCoursesController controller = new TempCoursesController();
        String url = URL.URL_GET_COURSELIST.replace("{id}", User.getUser(MainActivity.this).getId().toString());
        controller.getTempCourses(MainActivity.this, url);
    }

    private void checkForNewAppointments() {
        AppointmentController controller = new AppointmentController();
        String url = URL.URL_GET_ACTIVITIES.replace("{id}", User.getUser(MainActivity.this).getId().toString());
        controller.getAppointments(MainActivity.this, url);
    }

    private void setupWebSocketConnection() {
        Draft[] drafts = {new Draft_6455()};
        User user = User.getUser(this);
        if (user == null || user.getUsername() == null) {
            return;
        }
        String w = "ws://10.0.2.2:8080/websocket/login/" + user.getUsername();

        try {
            Log.d("Socket:", "Trying socket");
            cc = new WebSocketClient(new URI(w), drafts[0]) {
                @Override
                public void onMessage(String message) {
                    Log.d("", "run() returned: " + message);
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d("OPEN", "run() returned: " + "is connecting");
                    // Send "true" when the app starts (user is logged in)
                    cc.send("true");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                }

                @Override
                public void onError(Exception e) {
                    Log.d("Exception:", e.toString());
                }
            };
        } catch (URISyntaxException e) {
            Log.d("Exception:", e.getMessage().toString());
            e.printStackTrace();
        }
        cc.connect();
    }


    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    public void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray();

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = User.getUser(MainActivity.this);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        //if not logged in intent to
        if (user == null || user.getType() == LoggedInStates.NOT_LOGGED_IN) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (user.viewType != null) {
            switch (user.viewType) {
                case Monthly:
                    break;
                case Weekly:
                    Intent intentW = new Intent(MainActivity.this, WeeklyViewActivity.class);
                    startActivity(intentW);
                    break;
                case Daily:
                    Intent intentD = new Intent(MainActivity.this, DailyViewActivity.class);
                    startActivity(intentD);
                    break;
            }
        }

        //Logic for setting a schedule when a kid is added
        if ((user.getFriendsList() != null && user.getFriendsList().size() > 0 && User.selectedFriend == null)
                && user.getAccountType() != AccountType.NORMAL_USER) {

            User.selectedFriend = user.getFriendsList().get(user.getFriendsList().size() - 1);
        }

        if ((user.getAccountType() == AccountType.PARENT_USER && (user.getFriendsList() == null || user.getFriendsList().size() == 0))
                || user.getAccountType() == AccountType.ADVISER && (user.getFriendsList() == null || user.getFriendsList().size() == 0)) {
            if (user.getAccountType() == AccountType.ADVISER) {

                toolbar.setTitle("Select a Student to View");
            } else {
                toolbar.setTitle("Select your Dependent to View");
            }
            calendarRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            calendarRecyclerView.setVisibility(View.VISIBLE);
            setMonthView();
            if (User.selectedFriend != null && user.getAccountType() != AccountType.NORMAL_USER) {
                toolbar.setTitle("Schedule of: " + User.selectedFriend.getUsername());
            }
        }


    }

    @Override
    public void onItemLongClick(int position, LocalDate date) {
        String message = "LongPress on Button clicked!!";
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        Log.d("MainActivity", message);

        startActivity(new Intent(this, AppointmentEditActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Send "false" when the app is closed (user is logged out)
        if (cc != null && cc.isOpen()) {
            cc.send("false");
            cc.close();
        }
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView();

        }
    }
}
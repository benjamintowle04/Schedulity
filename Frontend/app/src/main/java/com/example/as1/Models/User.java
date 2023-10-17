package com.example.as1.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.as1.Enum.AccountType;
import com.example.as1.Enum.LoggedInStates;
import com.example.as1.Enum.ViewType;
import com.example.as1.FriendsDetailActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Integer Id;
    private String username;
    private String password;
    private String email;
    private AccountType accountType;
    private LoggedInStates type;

    private List<Friend> friendsList;
    private List<Course> coursesList;
    public List<Appointment> appointmentList;
    private List<Extracurricular> activityList;
    private int minutesPerWorkout;
    private int gymDays;
    private int studyHours;
    private SleepTime sleepTime;

    public ViewType viewType = ViewType.Monthly;

    private List<Friend> kidsList;
    private List<Friend> studentsList;
    private int parentReq;
    private Friend parent;
    private Friend child;
    public static Friend selectedFriend;

//    private static Gson gson = GsonUtil.createGson();


    public User(String name, String password, String email, LoggedInStates type, AccountType accountType) {
        this.username = name;
        this.password = password;
        this.email = email;
        this.type = (type == null) ? LoggedInStates.NOT_LOGGED_IN : type;
        this.accountType = (accountType == null) ? AccountType.NORMAL_USER : accountType;
        this.appointmentList = new ArrayList<>();
    }

    public void setType(LoggedInStates type) {
        this.type = type;
    }

    public LoggedInStates getType() {
        return this.type;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public List<Friend> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<Friend> friendsList) {
        this.friendsList = friendsList;
    }

    public void addFriendsToList(Friend friend) {
        if (this.friendsList == null) {
            this.friendsList = new ArrayList<>();
        }
        friendsList.add(friend);
    }

    public void removeFriendsFromList(Friend friend) {
        if (this.friendsList != null) {
            friendsList.remove(friend);
        }
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }


    public void addAppointmentToList(Appointment appointment) {
        if (this.appointmentList == null) {
            this.appointmentList = new ArrayList<>();
        }
        appointment.setId(appointmentList.size());
        appointmentList.add(appointment);
    }

    public void addAppointmentListToList(List<Appointment> appointment) {
        for (int i = 0; i < appointment.size(); i++) {
            Appointment tempAppointment = appointment.get(i);
            if (this.appointmentList == null) {
                this.appointmentList = new ArrayList<>();
            }
            tempAppointment.setId(appointmentList.size());

            if (!appointmentList.contains(tempAppointment))
                appointmentList.add(tempAppointment);
        }
    }


    public void removeAppointmentFromList(Appointment appointment) {
        if (this.appointmentList != null) {
            appointmentList.remove(appointment);
        }
    }

    public void removeAppointmentById(int id) {
        Appointment appointmentToRemove = null;
        for (Appointment appointment : appointmentList) {
            if (appointment.getId() == id) {
                appointmentToRemove = appointment;
                break;
            }
        }
        if (appointmentToRemove != null) {
            appointmentList.remove(appointmentToRemove);
        }
    }

    public Appointment getAppointmentById(int id) {
        for (Appointment appointment : appointmentList) {
            if (appointment.getId() == id) {
                return appointment;
            }
        }
        return null;
    }

    public List<Course> getCoursesList() {
        return coursesList;
    }

    public void setCourseTime(double time, int index) {
        this.coursesList.get(index).setTime(time);
    }

    public void setCoursesList(List<Course> list) {
        this.coursesList = list;
    }


    public void addCourseToList(Course course) {
        if (this.coursesList == null) {
            this.coursesList = new ArrayList<>();
        }
        coursesList.add(course);
    }

    public void removeCourseFromList(Course course) {
        if (this.coursesList != null) {
            coursesList.remove(course);
        }
    }

    public void addStudentsToList(Friend student) {
        if (this.studentsList == null) {
            this.studentsList = new ArrayList<>();
        }
        studentsList.add(student);
    }

    public void removeStudentFromList(Friend student) {
        if (this.studentsList != null) {
            studentsList.remove(student);
        }
    }

    public void addKidsToList(Friend kid) {
        if (this.kidsList == null) {
            this.kidsList = new ArrayList<>();
        }
        kidsList.add(kid);
    }

    public void disown(Friend kid) {
        if (this.kidsList != null) {
            kidsList.remove(kid);
        }
    }

    public void setKidsList(ArrayList<Friend> kids) {
        this.kidsList = kids;
    }

    public void setStudentsList(ArrayList<Friend> students) {
        this.studentsList = students;
    }

    public List<Friend> getKidsList() {
        return this.kidsList;
    }

    public List<Friend> getStudentsList() {
        return this.studentsList;
    }


    public static void saveUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences("UserObject", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("User", new Gson().toJson(user));
        editor.commit();
    }

    public void saveUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("UserObject", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("User", new Gson().toJson(this));
        editor.commit();
    }

    public static User getUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("UserObject", Context.MODE_PRIVATE);
        String userJson = preferences.getString("User", "");
        return new Gson().fromJson(userJson, User.class);
    }

    public Appointment getAppointmentByName(String name) {
        for (Appointment appointment : appointmentList) {
            if (appointment.getName().equals(name)) {
                return appointment;
            }
        }
        return null;
    }

    public static Boolean removeUser(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences("UserObject", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("User");
            editor.apply();
            return true;
        } catch (Throwable t) {
            Log.e("TAG", "Error message", t);
            System.out.println("Error " + t.getMessage().toString());
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User {\n");
        sb.append("  Id: ").append(Id).append("\n");
        sb.append("  username: ").append(username).append("\n");
        sb.append("  password: ").append(password).append("\n");
        sb.append("  email: ").append(email).append("\n");
        sb.append("  accountType: ").append(accountType).append("\n");
        sb.append("  type: ").append(type).append("\n");
        sb.append("  friendsList: ").append(friendsList).append("\n");
        sb.append("  coursesList: ").append(coursesList).append("\n");
        sb.append("  appointmentList: ").append(appointmentList).append("\n");
        sb.append("}");

        return sb.toString();
    }

    public void setMinutesPerWorkout(int min) {
        this.minutesPerWorkout = min;
    }

    public void setGymDays(int days) {
        this.gymDays = days;
    }

    public int getGymDays() {
        return this.gymDays;
    }

    public int getMinutesPerWorkout() {
        return this.minutesPerWorkout;
    }

    public void setStudyHours(int hours) {
        this.studyHours = hours;
    }

    public int getStudyHours() {
        return this.studyHours;
    }

    public void setSleepTime(SleepTime time) {
        this.sleepTime = time;
    }

    public SleepTime getSleepTime() {
        return this.sleepTime;
    }

    public void setParent(Friend parent) {this.parent = parent;}
    public Friend getParent() {return this.parent;}
    public void setChild(Friend child) {this.child = child;}
    public Friend getChild() {return this.child;}


    public void addToActivityList(Extracurricular extra) {
        if (this.activityList == null) {
            this.activityList = new ArrayList<Extracurricular>();
        }
        this.activityList.add(extra);
    }

    public static int GetHighestGroupId(List<Appointment> appointmentList) {
        int id = 0;
        for (Appointment appointment : appointmentList) {
            if (appointment.getGroupId() != null && appointment.getGroupId() > id) {
                id = appointment.getGroupId();
            }
        }
        return id;
    }

    public List<Extracurricular> getActivityList() {
        return this.activityList;
    }

    public void setActivityList(List<Extracurricular> list) {
        this.activityList = list;
    }

    public void setParentReq(int req) {this.parentReq = req;}
    public int getParentReq() {return this.parentReq;}

    public void clearActivityList() {
        if (this.activityList == null) {
            return;
        }
        int len = this.activityList.size();
        for (int i = 0; i < len; i++) {
            this.activityList.remove(0);
        }
    }

    public void clearCoursesList() {
        if (this.coursesList == null) {
            return;
        }
        int len = this.coursesList.size();
        for (int i = 0; i < len; i++) {
            this.coursesList.remove(0);
        }
    }

    public void initializeExtrasList() {
        this.activityList = new ArrayList<Extracurricular>();
    }

}


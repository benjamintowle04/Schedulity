package onetomany.Users;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.transaction.Transactional;
import javax.persistence.OneToOne;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import onetomany.Advisor.Advisor;
import onetomany.Courses.Course;
import onetomany.Parents.Parent;
import onetomany.StudyTime.StudyTime;

import onetomany.Activity.Activity;

import onetomany.SleepTime.SleepTime;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonIgnore;



/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@Entity
@Transactional
public class User {

     /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    /**
     * variables:
     * full nameXXXXXX
     * username
     * password
     * int type
     * courses(AwwayList)
     */
    @ApiModelProperty(notes = "User id", example = "1", required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty(notes = "Online?", example = "false", required = false)
    private boolean loggedIn;

    @ApiModelProperty(notes = "User email", example = "exEmail@Example.ex", required = true)
    private String email;

    @ApiModelProperty(notes = "Username", example = "exUsername", required = true)
    private String username;

    @ApiModelProperty(notes = "User Password", example = "password", required = true)
    private String password;


    @ApiModelProperty(notes = "Type of User", example = "1", required = false)
    private int type = 1;
    @ApiModelProperty(notes = "User Friends", example = "friendName", required = false)
    private ArrayList<String> friends_usernames;

    @ApiModelProperty(notes = "Parent has requested this child", example = "1", required = false)
    private int parentReq = 0;

    @ApiModelProperty(notes = "User's parent's name", example = "mom", required = false)
    private String parentName = "";

    @ApiModelProperty(notes = "User's advisors's name", example = "advisor", required = false)
    private String advisorName = "";

    @ApiModelProperty(notes = "User's Study hours", example = "18.0", required = false)
    private double stHours = 0.0;

    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * cascade is responsible propagating all changes, even to children of the class Eg: changes made to laptop within a user object will be reflected
     * in the database (more info : https://www.baeldung.com/jpa-cascade-types)
     * @JoinColumn defines the ownership of the foreign key i.e. the user table will have a field called laptop_id
     */
    @ApiModelProperty(notes = "User's courses", required = false)
    @ManyToMany
    @JoinTable(name = "User_Courses", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
//    @JoinColumn(name = "courses_id")
//    @JsonIgnore
    private List<Course> courses;

    @ApiModelProperty(notes = "User's Study Times", required = false)
    @OneToMany
    @JoinColumn(name = "study_times_id")
//    @JsonIgnore

    private List<StudyTime> studyTimes;

    @ApiModelProperty(notes = "User's Activities", required = false)
    @OneToMany
    @JoinColumn(name = "activities_id")
    private List<Activity> activities  = new ArrayList<>();

    @ApiModelProperty(notes = "User's Sleep Times", required = false)
    @OneToOne
    @JoinColumn(name = "sleep_time_id")
    private SleepTime sleepTime;

    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="friends_with",
            joinColumns={@JoinColumn(name="user_id")},
            inverseJoinColumns={@JoinColumn(name="friend_id")})
    @JsonIgnore
    private List<User> friends;

    @OneToOne
    @JoinColumn(name = "parents_id")
    @JsonIgnore
    private Parent parent = null;

    @ManyToOne
    @JoinColumn(name = "advisor_id")
    @JsonIgnore
    private Advisor advisor = null;

//    @ManyToMany(mappedBy="friends")
//    @JsonIgnore



    //creates user (constructor)
    public User(String username, String password, int type, String email) {
//        this.name = name;
        this.email = email;
//        this.ifActive = true;
        this.password = password;
        this.username = username;
        this.type = type;
        loggedIn = true;
        courses = new ArrayList<>();
        studyTimes = new ArrayList<>();
        friends = new ArrayList<>();
        friends_usernames = new ArrayList<>();
//        parentReq = 0;
    }

    //creates empty user
    public User() {
        courses = new ArrayList<>();
        friends = new ArrayList<>();
        friends_usernames  = new ArrayList<>();
        studyTimes = new ArrayList<>();
//        parentReq = 0;
    }

    //returns password
    public String getPassword() {
        return password;
    }
// =============================== Getters and Setters for each field ================================== //
    //changes password
    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //returns id of user
    public int getId(){
        return id;
    }

    //changes id if needed
    public void setId(int id){
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    //get the list of courses
    public List<Course> getCourses() {
        return courses;
    }

    //add a course to a user
    public void addCourse(Course course){
        courses.add(course);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<StudyTime> getStudyTimes() {
        return studyTimes;
    }

    public void setStudyTimes(StudyTime studyTime) {
        studyTimes.add(studyTime);
    }

    public List<Activity> getActivities(){return activities;}

    public void setActivities(Activity activity){activities.add(activity);}
    public SleepTime getSleepTime(){return sleepTime;}

    public void setSleepTime(SleepTime sleepTime){this.sleepTime = sleepTime;}

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(User friend) {

//        friends.add(friend);
//        friend.setFriends(this);
//        friends.remove(friends.size()-1);

            friends_usernames.add(friend.getUsername());

        friends.add(friend);


    }

    public void removeFriend(int index)
    {
        User u = friends.remove(index);
        for(int i = 0; i < friends_usernames.size(); i++)
        {
            if(u.getUsername() == friends_usernames.get(i))
            {
                friends_usernames.remove(i);
            }
        }
    }

    public ArrayList<String> getFriends_usernames() {
        return friends_usernames;
    }

    public void setFriends_usernames(String friend_username) {
        friends_usernames.add(friend_username);
    }

    public int getParentReq() {
        return parentReq;
    }

    public void setParentReq(int parentReq) {
        this.parentReq = parentReq;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Advisor getAdvisor() {
        return advisor;
    }

    public void setAdvisor(Advisor advisor) {
        this.advisor = advisor;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public void setAdvisorName(String advisorName) {
        this.advisorName = advisorName;
    }

    public double getStHours(){return stHours;}

    public void setStHours(double stHours){this.stHours = stHours;}
}

package onetomany.Parents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import onetomany.Users.User;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;
import javax.transaction.Transactional;

@Entity
//@Transactional
public class Parent {
    @ApiModelProperty(notes = "Parent id?", example = "1", required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty(notes = "Online?", example = "false", required = false)
    private boolean loggedIn;

    @ApiModelProperty(notes = "Parent email", example = "exEmail@Example.ex", required = true)
    private String email;

    @ApiModelProperty(notes = "Parent Username", example = "exUsername", required = true)
    private String username;

    @ApiModelProperty(notes = "Parent Password", example = "password", required = true)
    private String password;

    @ApiModelProperty(notes = "Type of User", example = "1", required = false)
    private int type = 2;

    @ApiModelProperty(notes = "User id for user requested for child", example = "1", required = false)
    private int request;

    @ApiModelProperty(notes = "Child's username", example = "exUsername", required = false)
    private String childName = "";

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User Child;




    public Parent(String username, String password, int type, String email)
    {
        this.username = username;
        this.password = password;
        this.type = type;
        this.email = email;
        request = 0;
    }

    public Parent()
    {

//        request = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public User getChild() {
        return Child;
    }

    public void setChild(User child) {
        Child = child;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }
}

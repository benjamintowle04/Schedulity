package onetomany;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import onetomany.Advisor.Advisor;
import onetomany.Advisor.AdvisorController;
import onetomany.Advisor.AdvisorRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import onetomany.Advisor.Advisor;
import onetomany.Advisor.AdvisorRepository;


import io.swagger.annotations.*;
import onetomany.Courses.CourseController;
import onetomany.Parents.Parent;
import onetomany.Parents.ParentController;
import onetomany.Parents.ParentRepository;
import onetomany.Activity.Activity;
import onetomany.Activity.ActivityController;
import onetomany.SleepTime.SleepTime;
import onetomany.StudyTime.StudyTime;
import onetomany.StudyTime.StudyTimeRepository;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.bind.annotation.*;
import onetomany.Users.User;



import onetomany.Courses.Course;
import onetomany.Courses.CourseRepository;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@RunWith(SpringRunner.class)
public class ScedulityTest {


    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }
        CourseController courseCont;


        @Test
        public void lizzyGetCourseTest(){when().request("GET", "/courses").then().statusCode(200);}
    


    @Test
    public void lizzyCourseAdd(){

        JSONObject course =  new JSONObject();

        try{
            course.put("course", "Junit 101");
            course.put("startTime", 11.0);
            course.put("endTime", 12.0);
            course.put("credits", 4);
            course.put("location", "Junit Lab");
            course.put("days", "R");
            course.put("startDate", "6-17-2023");
            course.put("endDate", "9-10-2023");
            course.put("notes", "TESTING");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Response response = RestAssured.given().header("Content-Type", "application/json").body(course.toString()).when().post("/course/" + 1);


        int status = response.getStatusCode();

        if(status != 200)
            throw new RuntimeException("Error making course");
//
//        JsonPath pathEvaluator = response.jsonPath();
//
//        String courseTest = pathEvaluator.get("course");
//
//        String str = response.body().asString();
//
//        Assert.assertEquals(courseTest, "Junit 101");


    }

    @Test
    public void lizzyCourseDelete(){

        Response response = RestAssured.given().header("Content-Type", "application/json").when().delete("/course/delete/" + 10);


        int status = response.getStatusCode();

        if(status != 200)
            throw new RuntimeException("Error making course");

    }

    @Test
    public void lizzyActivityPut(){

        JSONObject activity =  new JSONObject();

        try{
            activity.put("startTime", 23.0);
            activity.put("endTime", 23.5);
            activity.put("actName", "JunitTest");
            activity.put("groupId", 1);
            activity.put("location", "Place");
            activity.put("recurring", true);
            activity.put("day", "1/1/2023");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Response response = RestAssured.given().header("Content-Type", "application/json").body(activity.toString()).when().put("/activity/group/1/1");


        JsonPath pathEvaluator = response.jsonPath();

        System.out.println(response.asString());

        Assert.assertEquals(response.asString(), "{\"message\":\"failure\"}");

    }

    @Test
    public void lizzyPostSleepTime(){

        JSONObject sTime =  new JSONObject();

        try{
            sTime.put("weekdayHours", 8.0);
            sTime.put("weekendHours", 9.0);
            sTime.put("weekdayStartTime", 23.0);
            sTime.put("weekendStartTime", 1.0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Response response = RestAssured.given().header("Content-Type", "application/json").body(sTime.toString()).when().post("/sleepTime/" + 1);

        JsonPath pathEvaluator = response.jsonPath();

        String timeTest = pathEvaluator.get("weekendEndTime").toString();

        String str = response.body().asString();

        Assert.assertEquals("10.0", timeTest);

    }

}


//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//
//@RunWith(SpringRunner.class)
//public class UserControllerTest {
//
//    @LocalServerPort
//    int port;
//
//    @Before
//    public void setUp() {
//        RestAssured.port = port;
//        RestAssured.baseURI = "http://localhost";
//    }
//
//    @Test
//    public void usernameFailedTest() {
//
//        // Send request and receive response
//        Response response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset", "utf-8").
//                body("").
//                when().
//                get("/users/id/58");
//
//
//        // Check status code
//        int statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//
//        // Check response body for correct response
//        String returnString = response.getBody().asString();
//        try {
//            JSONObject returnObj = new JSONObject(returnString);
//            assertEquals("failed", returnObj.get("username"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//}

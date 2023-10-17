package onetomany;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import onetomany.Users.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@RunWith(SpringRunner.class)
public class WavesTests {
    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void whenRequestedPost_thenCreated() {
        //String username, String password, int type, String email
        User user = new User("waves","password",1,"waves@ocean.net");

        Response response = RestAssured.given().header("Content-Type","application/json").body(user.toString()).when().post("/users/register");




            with().body(new User("ralphy", "234", 1, "X"))
                    .when()
                    .request("POST", "/users/register")
                    .then()
                    .statusCode(415);

    }

    @Test
    public void whenRequestHead_thenOK() {
        when().request("GET", "/users").then().statusCode(200);
    }

    @Test
    public void add_FriendTest()
    {
        User user = new User("waves","password",1,"waves@ocean.net");

        User user2 = new User("lizzy","1234",1,"lizzy@zoo.org");

    }


}

package onetomany.Users;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import onetomany.Parents.Parent;
import onetomany.Parents.ParentController;
import onetomany.Parents.ParentRepository;
import onetomany.Activity.Activity;
import onetomany.SleepTime.SleepTime;
import onetomany.StudyTime.StudyTime;
import onetomany.StudyTime.StudyTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



import onetomany.Courses.Course;
import onetomany.Courses.CourseRepository;

/**
 * 
 * @author Vivek Bengre
 * 
 */

/**
 * methods to implement
 *
 * 1. users to list the users and all data *DONE*
 * 2. find a user by id *DONE*
 * 3. create a user from request body *DONE*
 * 4. find user by username
 * 5. update a username (must know what password is)
 * 6. update a password (must know username/email and previous password)
 * 7. add a class
 * 8. delete a user (must know username/email and password)
 */
@Api(value = "User Controller", description = "All Student type functions plus more!")
@RestController
public class  UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepo;

    @Autowired
    StudyTimeRepository StudyRepo;

    @Autowired
    ParentRepository parentRepository;

    @Autowired
    ParentController parentController;

    @Autowired
    AdvisorRepository advisorRepository;


    @Autowired
    AdvisorController advisorController;



    //for when code succeeds
    private String success = "{\"message\":\"success\"}";
    //for when code fails
    private String failure = "{\"message\":\"failure";

    private String successID = "{\"message\":\"success\"," + "\n" + "\"id\":";

    /**
     *  lists all users
     * @return list of all users
     */
    @ApiOperation(value = "Get list of Users", response = Iterable.class, tags = "Students")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    /**
     * finds the given user by their id
     * @param id
     * @return user with id
     */
    @ApiOperation(value = "Gets a User based on id", notes = "Returns a user for the given id", response = User.class, tags = "Students")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable ("id")
    @ApiParam(name = "id",value = "User id", example = "1") int id) {
        return userRepository.findById(id);
    }

    /**
     * creates a user from a json body
     * @param user
     * @return a string saying success or no success
     */
    @ApiOperation(value = "Creates a new User and saves it to database", response = String.class, tags = "Students")
    @PostMapping(path = "/users/register")
    String createUser(@ApiParam(name = "User info", value = "User's username, email and password \r\n Example = \"{\"username\":\"exUname\",\"password\":\"exPassword\",\"email\":\"exEmail\",\"type\":1}\"")@RequestBody User user){

        List<Parent> PList = parentRepository.findAll();
        List<Advisor> AList = advisorRepository.findAll();
        List<User> UList = userRepository.findAll();
        Parent p;
        Advisor a;
        User u;

        if ((user.getEmail() == null) || (user.getPassword() == null) || (user.getUsername() == null))
        {
            return failure + ": null Request\"}";
        }

        for (User value : UList) {
            u = value;
            if (user.getUsername().equals(u.getUsername()) || user.getEmail().equals(u.getEmail())) {
                return failure + ": Username or Email already in use\"}";
            }
        }

        for (Advisor value : AList) {
            a = value;
            if (user.getUsername().equals(a.getUsername()) || user.getEmail().equals(a.getEmail())) {
                return failure + ": Username or Email already in use\"}";
            }
        }

        for (Parent value : PList) {
            p = value;
            if (user.getUsername().equals(p.getUsername()) || user.getEmail().equals(p.getEmail())) {
                return failure + ": Username or Email already in use\"}";
            }
        }

        userRepository.save(user);
        return successID + user.getId() + "}";
    }

    @ApiOperation(value = "Logs into your account", response = String.class, tags = "Students")
    @PostMapping(path = "/users/login")
    String login(@RequestBody User user)
    {

        if(user.getUsername().equals("")){return failure + ": invalid username\"}";}

        if(user.getPassword().equals("")){return failure + ": invalid password\"}";}

        if(user.getEmail().equals("")){return failure + ": invalid email\"}";}

        List<User> UL = userRepository.findAll();

        List<Parent> PL = parentRepository.findAll();

        List<Advisor> AL = advisorRepository.findAll();

        if(UL.size() != 0)
        {
            for(int i = 0; i < UL.size();i++)
            {
                if(user.getUsername().equals(UL.get(i).getUsername()))
                {
                    User result = UL.get(i);
                    if(!(result.getPassword().equals(user.getPassword())))
                    {
                        return failure + ": invalid password\"}";
                    }
                    if(!(result.getEmail().equals(user.getEmail())))
                    {
                        return failure + ": invalid email\"}";
                    }
                    result.setLoggedIn(true);
                    userRepository.save(result);
                    return successID + result.getId() + "}";
                }
            }
        }

        if(PL.size() != 0)
        {
            for(int i = 0; i < PL.size();i++)
            {
                if(user.getUsername().equals(PL.get(i).getUsername()))
                {
                    Parent result = PL.get(i);
                    if(!(result.getPassword().equals(user.getPassword())))
                    {
                        return failure + ": invalid password\"}";
                    }
                    if(!(result.getEmail().equals(user.getEmail())))
                    {
                        return failure + ": invalid email\"}";
                    }
                    result.setLoggedIn(true);
                    parentRepository.save(result);

                        return successID + result.getId() + "}";
                    
                }
            }
        }

        if(AL.size() != 0)
        {
            for(int i = 0; i < AL.size();i++)
            {
                if(user.getUsername().equals(AL.get(i).getUsername()))
                {
                    Advisor result = AL.get(i);
                    if(!(result.getPassword().equals(user.getPassword())))
                    {
                        return failure + ": invalid password\"}";
                    }
                    if(!(result.getEmail().equals(user.getEmail())))
                    {
                        return failure + ": invalid email\"}";
                    }
                    result.setLoggedIn(true);
                    advisorRepository.save(result);
                    return successID + result.getId() + "}";
                }
            }
        }
        return failure + ": invalid username\"}";
    }

    @PostMapping("/users/{username}/logout")
    String logout(@PathVariable String username)
    {
        User u = findByUsername(username);
        Advisor a = advisorController.findByUsername(username);
        Parent p = parentController.findByUsername(username);

        if(!(u == null))
        {
            u.setLoggedIn(false);
            userRepository.save(u);
            return success;
        }
        if(!(a == null))
        {
            a.setLoggedIn(false);
            advisorRepository.save(a);
            return success;
        }
        if(!(p == null))
        {
            p.setLoggedIn(false);
            parentRepository.save(p);
            return success;
        }
        return failure + ": invalid username\"}";
    }


    @ApiOperation(value = "Adds a friend based off your id and your friends id", response = String.class, tags = "Students")
    @PostMapping("/users/{id}/addFriend")
    String addFriend(@PathVariable ("id") @ApiParam(name = "id",value = "User id", example = "1")int id,
    @ApiParam(name = "fi",value = "Friends id", example = "1")@RequestBody String fi)
    {
        String[] parts = fi.split("\"");

        String frids = parts[3];

        int fid;
        fid = Integer.parseInt(frids);
        User user = userRepository.findById(id);
        User friend = userRepository.findById(fid);
        if(user.equals(null))
        {
            return failure + ": no user with that id\"}";
        }
        else if(friend.equals(null))
        {
            return failure + ": no friend with that id\"}";
        }
        else if(id == fid)
        {
            return failure + ": cannot add yourself\"}";
        }
        List<User> fL = user.getFriends();
        for(int i = 0; i < user.getFriends().size();i++)
        {
            if(fL.get(i).getId() == fid)
            {
                return failure + ": Already friends\"}";
            }
        }
        user.setFriends(friend);
        friend.setFriends(user);
//        friend.setFriends_usernames(user.getUsername());
        userRepository.save(friend);
        user.removeFriend(user.getFriends().size()-1);
        user.setFriends(friend);
//        user.setFriends_usernames(friend.getUsername());
        userRepository.save(user);
        return success;
    }

    @ApiOperation(value = "removes a friend based off your id and your friends id", response = String.class, tags = "Students")
    @PostMapping("/users/{id}/removeFriend")
    String removeFriend(@PathVariable ("id") @ApiParam(name = "id",value = "User id", example = "1")int id,
    @ApiParam(name = "fi",value = "Friends id", example = "1")@RequestBody String fi)
    {
        String[] parts = fi.split("\"");

        String frids = parts[3];

        int fid;
        fid = Integer.parseInt(frids);
        User user = userRepository.findById(id);
        User friend = userRepository.findById(fid);
        if(user.equals(null))
        {
            return failure + ": No user with that id\"}";
        }
        else if(friend.equals(null))
        {
            return failure + ": No friend with that username\"}";
        }
        else if(user.getId() == friend.getId())
        {
            return failure + ": Cannot add yourself\"}";
        }
        List<User> FL = user.getFriends();
        for(int i = 0;i < FL.size();i++)
        {
            if(FL.get(i).getId() == friend.getId())
            {
                user.getFriends_usernames().remove(i);
                user.getFriends().remove(i);
            }
        }

        FL = friend.getFriends();
        for(int j = 0;j < FL.size(); j++)
        {
            if(FL.get(j).getId() == user.getId())
            {
                friend.getFriends_usernames().remove(j);
                friend.getFriends().remove(j);
                userRepository.save(user);
                userRepository.save(friend);
                return success;
            }
        }
        return failure + ": You don't have them added as a friend\"}";
    }

    @ApiOperation(value = "Gets a list of all your friends", response = String.class, tags = "Students")
    @GetMapping("/users/{id}/getFriends")
    public List<User> getFriends(@PathVariable ("id") @ApiParam(name = "id",value = "User id", example = "1") int id)
    {
        User user = userRepository.findById(id);
        if(user.equals(null))
        {
            return null;
        }
        else
        {
            return user.getFriends();
        }
    }

    @ApiOperation(value = "Gets a list of all of the people you're not friends with", response = String.class, tags = "Students")
    @GetMapping("/users/{id}/NonFriends")
    public List<User> getNonFriends(@PathVariable ("id") @ApiParam(name = "id",value = "User id", example = "1") int id)
    {

        List<User> result = userRepository.findAll();

//        List<User> result = new ArrayList<>();
//        List<User> users =  new ArrayList<>();
//        users = userRepository.findAll();
        User user = userRepository.findById(id);
        List<User> friends = user.getFriends();
        User cur;

        int size = result.size();

        for(int i = size - 1; i >= 0;i--)
        {
            cur = result.get(i);
            for(int j = 0; j < friends.size();j++)
            {
                if(cur.getId() == friends.get(j).getId() || cur.getId() == id)
                {
                    result.remove(i);
                }
            }

        }

        return result;
    }

    /**
     * updates user from a json body (not good for practical use because must verify
     * user knows their username before changing information
     * @param id
     * @param request
     * @return the updated user
     */
    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        request.setId(id);
        userRepository.save(request);
        return userRepository.findById(id);
    }

    /**
     * adds a course to a users list of courses
     * @param userId
     * @param courseId
     * @return a string for success or failure
     */
    @ApiOperation(value = "assigns a course found by id to your user", response = String.class, tags = "Students")
    @PutMapping("/users/{userId}/courses/{courseId}")
    public String assignCourseToUser(@PathVariable ("userId") @ApiParam(name = "userId",value = "User id", example = "1") int userId,@PathVariable ("courseId") @ApiParam(name = "courseId",value = "Course id", example = "1") int courseId){
        User user = userRepository.findById(userId);
        Course c = courseRepo.findById(courseId);
        if(user == null || c == null)
            return failure + ": null error\"}";

        //if found in user
        boolean found = false;

        List<User> curr = c.getUsers();

        //if course has that user already do not add
        for(int i = 0; i < curr.size(); i++){

            User u = curr.get(i);

            if(u.equals(user)){
                found = true;
            }

        }

        if(!found) {
            //should work
            c.getUsers().add(user);

            //does not work right
            user.addCourse(c);
        }

        userRepository.save(user);
        return success;
    }

    @ApiOperation(value = "assigns a study time to the respective user", response = String.class, tags = "Students")
    @PutMapping("/users/{userId}/StudyTime/{STID}")
    public String assignSTToUser(@PathVariable ("userId") @ApiParam(name = "userId",value = "User id", example = "1") int userId,@PathVariable ("STID") @ApiParam(name = "STID",value = "Study Time id", example = "1") int STID){
        User user = userRepository.findById(userId);
        StudyTime c = StudyRepo.findById(STID);
        if(user == null || c == null)
            return failure + ": null error\"}";
        c.setUser(user);
        user.setStudyTimes(c);
//        userRepository.save(user);
        return success;
    }

    /**
     * deletes a user based on id(not very practical because a user should know their username and password
     * before they can delete their user
     * @param id
     * @return
     */
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }

    @DeleteMapping(path = "/users/delete")
    String deleteUser2(@RequestBody User user){
        userRepository.delete(user);
        return success;
    }

    /**
     * changes username
     * @param id
     * @return success or failure
     */
    @ApiOperation(value = "changes the username based on the user you send in", response = String.class, tags = "Students")
    @PostMapping(path = "users/username/{id}")
    String changeUsername(@PathVariable ("id") @ApiParam(name = "id",value = "User id", example = "1") int id, @RequestBody User user)
    {
        User u = userRepository.findById(id);
        List<User> UserList = userRepository.findAll();

        if(user.getUsername().equals(null))
        {
            return failure + "No user with that username";
        }

        for(int i = 0; i < UserList.size();i++)
        {
            if(user.getUsername().equals(UserList.get(i).getUsername()))
            {
                return failure + "No user saved with that username";
            }
        }

        u.setUsername(user.getUsername());
        userRepository.save(u);
        return success;
    }

    @PostMapping(path = "users/password/{id}")
    String changePassword(@PathVariable int id, @RequestBody User user)
    {
        User u = userRepository.findById(id);
        List<User> UserList = userRepository.findAll();

        if(user.getPassword().equals(null)){
            return failure;
        }

        u.setPassword(user.getPassword());
        userRepository.save(u);
        return success;
    }

    @PostMapping(path = "users/email/{id}")
    String changeEmail(@PathVariable int id, @RequestBody User user)
    {
        User u = userRepository.findById(id);
        List<User> UserList = userRepository.findAll();

        if(user.getEmail().equals(null))
        {
            return failure;
        }

        for(int i = 0; i < UserList.size();i++)
        {
            if(user.getEmail().equals(UserList.get(i).getEmail()))
            {
                return failure;
            }
        }

        u.setEmail(user.getEmail());
        userRepository.save(u);
        return success;
    }

    /**
     * deletes a user but user must know their username and password to delete
     * @param id
     * @param uName
     * @param pass
     * @return success or failure
     */
    @DeleteMapping("users/delete/{id}/{uName}/{pass}")
    public String Delete_user(@PathVariable int id, @PathVariable String uName, @PathVariable String pass)
    {
        User user = getUserById(id);
        if(user == null)
        {
            return failure;
        }
        else if(uName.equals(user.getUsername()) && pass.equals(user.getPassword()))
        {
            userRepository.delete(user);
            return success;
        }
        else
        {
            return failure;
        }
    }

    /**
     * finds the user from a username (useful inside other methods)
     * @param uName
     * @return the user with that given username
     */
    @ApiOperation(value = "finds a given user based on the username alone", response = User.class, tags = "Students")
    @GetMapping("users/find/{uName}")
    public User findByUsername(@PathVariable ("uName") @ApiParam(name = "uName",value = "Username", example = "exUsername")String uName)
    {
        List<User> UList = userRepository.findAll();
        for(int i = 0; i < UList.size();i++)
        {
            if(UList.get(i).getUsername().equals(uName))
            {
                return UList.get(i);
            }
        }
        return null;
    }

    /**
     * changes password but must input username and old password to prove you can change it
     * @param oPass
     * @param uName
     * @param nPass
     * @return success or failure message
     */
    @PutMapping("users/{uName}/{oPass}/{nPass}")
    public String Change_Password(@PathVariable String oPass, @PathVariable String uName, @PathVariable String nPass) {
        User user = findByUsername(uName);
        if (user == null) {
            return failure;
        } else if (uName.equals(user.getUsername()) & oPass.equals(user.getPassword())) {
            user.setPassword(nPass);
            userRepository.save(user);
            return success;
        } else {
            return failure;
        }
    }


    @ApiOperation(value = "Accepts the parent that requested you as their child", response = String.class, tags = "Students")
    @PostMapping("users/{id}/AcceptParent/{pName}")
    public String AcceptParent(@PathVariable ("pName") @ApiParam(name = "pName",value = "Parent's username", example = "mom") String pName, @PathVariable ("id") @ApiParam(name = "id",value = "User id", example = "1")int id)
    {
        Parent parent = parentController.findByUsername(pName);
        User user = userRepository.findById(id);

        if(parent.equals(null) || user.equals(null))
        {
            return failure + "null user or parent";
        }
        else if(parent.getRequest() != id)
        {
            return failure + "no parent requested this child";
        }

        user.setParent(parent);
        user.setParentReq(0);
        user.setParentName(parent.getUsername());
        parent.setChild(user);
        parent.setChildName(user.getUsername());
        parent.setRequest(0);
        parentRepository.save(parent);

        user.setParent(parent);

        userRepository.save(user);
        return success;
    }

    @ApiOperation(value = "Declines the parent that requested you as their child", response = String.class, tags = "Students")
    @PostMapping("users/{id}/DeclineParent/{pName}")
    public String DeclineParent(@PathVariable ("pName") @ApiParam(name = "pName",value = "Parent's username", example = "mom")String pName, @PathVariable ("id") @ApiParam(name = "id",value = "User id", example = "1")int id) {
        Parent parent = parentController.findByUsername(pName);
        User user = userRepository.findById(id);

        if (parent.equals(null) || user.equals(null)) {
            return failure + "null user or parent";
        } else if (parent.getRequest() != id) {
            return failure + "no parent requested this child";
        }

        user.setParentReq(0);
        parent.setRequest(0);
        parentRepository.save(parent);
        userRepository.save(user);

        return success;
    }

    @PutMapping("users/deleteAct/{userId}/{actId}")
    public String deleteActFromUser(@PathVariable int userId, @PathVariable int actId){

        User user = userRepository.findById(userId);

        for(int i = 0; i < user.getActivities().size(); i++){

            if(i == actId){

                //delete from user
                user.getActivities().remove(i);
                userRepository.save(user);

                return success;

            }
        }

        return failure;

    }

    @GetMapping("/users/getDay/{id}/{month}/{date}/{day}/{year}")
    public List<Event> getDay(@PathVariable int id, @PathVariable int month,@PathVariable int date, @PathVariable char day, @PathVariable int year)
    {
        List<Event> events = new ArrayList<>();

        User user = userRepository.findById(id);

        List<Course> courses = user.getCourses();

        List<StudyTime> studyTimes = user.getStudyTimes();

        SleepTime sleepTime = user.getSleepTime();

        String DOY = "" + month + "-" + date + "-" + year;

        List<Activity> activities = user.getActivities();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M-d-y");
        LocalDate startDate;
        LocalDate endDate;
        LocalDate currDate = LocalDate.parse(DOY,formatter);

        for(int i = 0; i < courses.size();i++)
        {
            Course c = courses.get(i);

            String days = c.getDays();

            String cSDate = c.getStartDate();
            String cEDate = c.getEndDate();



            startDate = LocalDate.parse(cSDate, formatter);
            endDate = LocalDate.parse(cEDate, formatter);

            if(startDate.isBefore(currDate) && endDate.isAfter(currDate)) {
                for (int j = 0; j < days.length(); j++) {
                    char d = days.charAt(j);


                    if (d == day) {
                        events.add(new Event(c.getId(), c.getCourse(), "course", c.getStartTime(), c.getEndTime()));
                    }
                }
            }
        }

//        for(int i = 0; i < studyTimes.size();i++)
//        {
//            StudyTime sT = studyTimes.get(i);
//
//            String days = st.ge
//        }

        for(int i = 0; i < activities.size();i++)
        {
            Activity a = activities.get(i);

            String[] actDate = a.getDate().split("-");
            int actMonth = Integer.parseInt(actDate[0]);
            int actDay = Integer.parseInt(actDate[1]);
            int actYear = Integer.parseInt(actDate[2]);

            if(actMonth == month && actDay == date && a.getDay().charAt(0) == day && actYear == year){

                events.add(new Event(a.getId(), a.getActName(), "activity", a.getStartTime(),a.getEndTime()));
            }
        }



        if(sleepTime != null) {
            if (sleepTime.getWeekdayStartTime() > 12.0 && day != 'S' && day != 'U' && day != 'F') {
                events.add(new Event(sleepTime.getId(), "Sleep Time", "sleep", sleepTime.getWeekdayStartTime(), 24.0));
                events.add(new Event(sleepTime.getId(), "Sleep Time", "sleep", 0.0, sleepTime.getWeekdayEndTime()));
            } else if (day != 'S' && day != 'U' && day != 'F') {
                events.add(new Event(sleepTime.getId(), "Sleep Time", "sleep", sleepTime.getWeekdayStartTime(), sleepTime.getWeekdayEndTime()));
            } else if (sleepTime.getWeekendStartTime() > 12.0 && day == 'S') {
                events.add(new Event(sleepTime.getId(), "Sleep Time", "sleep", sleepTime.getWeekendStartTime(), 24.0));
                events.add(new Event(sleepTime.getId(), "Sleep Time", "sleep", 0.0, sleepTime.getWeekendEndTime()));
            } else if (day == 'S') {
                events.add(new Event(sleepTime.getId(), "Sleep Time", "sleep", sleepTime.getWeekendStartTime(), sleepTime.getWeekendEndTime()));
            } else if (day == 'F') {
                if (12.0 < sleepTime.getWeekdayStartTime()) {
                    events.add(new Event(sleepTime.getId(), "Sleep Time", "sleep", 0.0, sleepTime.getWeekdayEndTime()));
                    if (sleepTime.getWeekendStartTime() > 12.0) {
                        events.add(new Event(sleepTime.getId(), "Sleep Time", "sleep", sleepTime.getWeekendStartTime(), 24.0));
                    }
                } else {
                    events.add(new Event(sleepTime.getId(), "Sleep Time", "sleep", sleepTime.getWeekdayStartTime(), sleepTime.getWeekdayEndTime()));
                    if (sleepTime.getWeekendStartTime() > 12.0) {
                        events.add(new Event(sleepTime.getId(), "Sleep Time", "sleep", sleepTime.getWeekendStartTime(), 24.0));
                    }
                }
            } else if (day == 'U') {
                if (sleepTime.getWeekendStartTime() > 12.0) {
                    events.add(new Event(sleepTime.getId(), "Sleep Time", "sleep", 0.0, sleepTime.getWeekendEndTime()));
                    if (sleepTime.getWeekdayStartTime() > 12.0) {
                        events.add(new Event(sleepTime.getId(), "Sleep Time", "sleep", sleepTime.getWeekdayStartTime(), 24.0));
                    }
                } else {
                    events.add(new Event(sleepTime.getId(), "Sleep Time", "sleep", sleepTime.getWeekendStartTime(), sleepTime.getWeekendEndTime()));
                    if (sleepTime.getWeekdayStartTime() > 12.0) {
                        events.add(new Event(sleepTime.getId(), "Sleep Time", "sleep", sleepTime.getWeekdayStartTime(), 24.0));
                    }
                }
            }
        }

        List<Event> Cronologically = new ArrayList<>();
        for(int i = events.size() - 1;i >= 0;i--)
        {
            int index = 0;
            for(int j = 0; j < events.size();j++)
            {
                if(events.get(j).getStart() < events.get(index).getStart() )
                {
                    index = j;
                }
            }
            Cronologically.add(events.remove(index));
        }

        return Cronologically;
    }

    @GetMapping("users/getMonth/{id}/{Month}/{fDay}/{year}")
    public List<Event> getMonth(@PathVariable int id, @PathVariable int Month, @PathVariable char fDay, @PathVariable int year)
    {
        List<Event> results = new ArrayList<>();
        List<Event> day = new ArrayList<>();
        char[] days = {'U','M','T','W','R','F','S'};
        int startDay = -1;
        int size = 0;
        char d = 'x';

        for(int i = 0; i < 7; i++)
        {
            if(days[i] == fDay)
            {
                startDay = i;
            }
        }

        if(Month == 4 || Month == 6 || Month == 9 || Month == 11)
        {
            size = 30;
        }
        else if(Month == 2)
        {
            size = 28;
        }
        else
        {
            size = 31;
        }

        for(int i = 0;i < size;i++)
        {
            d = days[startDay];

            //int eId, String name, String eType, double start, double end
            day = getDay(id,Month,i + 1,d,year);
            int dm = i + 1;
            day.add(new Event(-1, "End of day for " + Month + "-" + dm, "DAY OVER", 0.0,0.0));

            for(int j = 0;j < day.size();j++)
            {
                results.add(results.size(), day.get(j));
            }

            startDay++;
            startDay = startDay % 7;
        }






        return results;
    }

}


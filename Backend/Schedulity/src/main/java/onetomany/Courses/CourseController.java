package onetomany.Courses;

import java.util.List;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import onetomany.Users.User;
import onetomany.Users.UserRepository;
import onetomany.Users.UserController;

import onetomany.Activity.Activity;
import onetomany.Activity.ActivityRepository;
import onetomany.Activity.ActivityController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 
 * @author Vivek Bengre
 * 
 */

@Api(value = "Course Controller", description = "All Course type functions!")
@RestController
public class CourseController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserController userController;

    @Autowired
    ActivityRepository actRepo;

    @Autowired
    ActivityController actController;
    
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @ApiOperation(value = "Get list of Courses", response = Iterable.class, tags = "Courses")
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    /**
     * Gets all the courses and outputs them
     * @return all the courses in repository
     */
    @GetMapping(path = "/courses")
    List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    /**
     * Gets what course it is based on the id that is input
     * @param id
     * @return course
     */
    @ApiOperation(value = "Get course by id",notes= "returns a course from the given id", response = Course.class, tags = "Courses")
    @GetMapping(path = "/course/get/{id}")
    Course getCourseById(@PathVariable ("id")
                         @ApiParam(name = "id", value = "course id", example = "1") int id){
        return courseRepository.findById(id);
    }

    @ApiOperation(value = "Get courses of given user",notes= "returns a course list for the given user", response = Iterable.class, tags = "Courses")
    @GetMapping(path = "courseList/{id}")
    List<Course> getCourseList(@PathVariable ("id")
                               @ApiParam(name = "id", value = "user id", example = "1") int id)
    {
        User user = userRepository.findById(id);
        return user.getCourses();
    }


    /**
     * creates a course and adds it to the data table
     *
     * @param course
     * @return failure or success
     */
    @PostMapping(path = "/course")
    String createCourse(@RequestBody Course course){

        if(course == null){
            return failure;
        }

        //all the courses
        List <Course> All = courseRepository.findAll();

        //if course has nothing return just previous courses
        if (course == null)
            return failure;

        //boolean to indictate if it is a duplicate
        boolean found = false;

        //finding if it is the same as another
        for(int i = 0; i < All.size(); i++){

            Course curr = All.get(i);

            //if it is in the table already
            if(curr.getCourse().equals(course.getCourse()) && curr.getDays().equals(course.getDays()) && curr.getStartTime() == course.getStartTime() && curr.getEndTime() == course.getEndTime() && curr.getStartDate().equals(course.getStartDate()) && curr.getEndDate().equals(course.getEndDate())){

                return failure;
            }

        }

        courseRepository.save(course);

        return success;

    }

    /**
     * Creates a course and adds it to the repository
     * @param course
     * @return all the courses that user is in
     */
    @ApiOperation(value = "Create course given a user", response = Course.class, tags = "Courses")
    @PostMapping(path = "/course/{userId}")
    Course createUserCourse(@PathVariable ("userId")
                        @ApiParam(name = "id", value = "course id", example = "1") int userId,
                        @ApiParam(name = "course info", value = "Course name, start time, end time, etc. \r\n Example {\"course\": \"Course 101\",\"startTime\": 9.0,\"endTime\": 10.0,\"credits\": 3,\"location\": \"Hall\",\"days\": \"T R\"}") @RequestBody Course course){
        //finds the user object
        User u = userRepository.findById(userId);

        //all the courses
        List <Course> All = courseRepository.findAll();

        //if course has nothing return just previous courses
        if (course == null)
            return null;

        //boolean to indictate if it is a duplicate
        boolean found = false;

        //finding if it is the same as another
        for(int i = 0; i < All.size(); i++){

            Course curr = All.get(i);

            //if it is in the table already
            if(curr.getCourse().equals(course.getCourse()) && curr.getDays().equals(course.getDays()) && curr.getStartTime() == course.getStartTime() && curr.getEndTime() == course.getEndTime() && curr.getStartDate().equals(course.getStartDate()) && curr.getEndDate().equals(course.getEndDate())){

                course = curr;
                found = true;

                break;
            }



        }

        //finding the variables
        String[] sCourseparts = course.getStartDate().split("-");
        int startMonthCourse = Integer.parseInt(sCourseparts[0]);
        int startDayCourse = Integer.parseInt(sCourseparts[1]);
        int startYearCourse = Integer.parseInt(sCourseparts[2]);

        String[] eCourseparts = course.getEndDate().split("-");
        int endMonthCourse = Integer.parseInt(eCourseparts[0]);
        int endDayCourse = Integer.parseInt(eCourseparts[1]);
        int endYearCourse = Integer.parseInt(eCourseparts[2]);

        //if the start year is greater than the end year
        if(startYearCourse > endYearCourse){
            return null;
        }

        //if the years are the same
        if(startYearCourse == endYearCourse){

            //months must be smaller than bigger
            if(startMonthCourse > endMonthCourse){
                return null;
            }

            //if they are the same
            if(startMonthCourse == endMonthCourse){

                //it can not start after it ends
                if(startDayCourse >= endDayCourse){
                    return null;
                }
            }

        }

        //sees if this time overlaps with any other time
        for(int i = 0; i < u.getCourses().size(); i++) {

            //current course
            Course c = u.getCourses().get(i);

            boolean overlap = false;

            //if they have any days in common
            for(int k = 0; k < c.getDays().length(); k++){

                char day = c.getDays().charAt(k);

                for(int j = 0; j < course.getDays().length(); j++){

                    if(day == course.getDays().charAt(j) && day != ' '){
                        overlap = true;
                    }

                }

            }

            //finds this courses variables
            String[] sCurrparts = c.getStartDate().split("-");
            int startMonthCurr = Integer.parseInt(sCurrparts[0]);
            int startDayCurr = Integer.parseInt(sCurrparts[1]);
            int startYearCurr = Integer.parseInt(sCurrparts[2]);

            String[] eCurrparts = c.getEndDate().split("-");
            int endMonthCurr = Integer.parseInt(eCurrparts[0]);
            int endDayCurr = Integer.parseInt(eCurrparts[1]);
            int endYearCurr = Integer.parseInt(eCurrparts[2]);


            //if the course is on the same days
            if (overlap) {

                //start time and end time to the factor of 100
                int sT = (int)(c.getStartTime() * 100);
                int eT = (int)(c.getEndTime() *100);

                //start and end of new course
                int courseS = (int)(course.getStartTime() * 100);
                int courseE = (int)(course.getEndTime() * 100);


                //looks through to see if this conflicts
                for(int j = sT; j <= eT; j++){


                    //if there is a conflict do not add
                    for(int k = courseS; k <= courseE; k++){

                        if(k == j){

                            boolean year = false;

                            //finds if years overlap
                            for( int a = startYearCourse; a <= endYearCourse; a++){

                                if(startYearCurr == a || endYearCurr == a){
                                    year = true;
                                }

                            }

                            //if the years all overlap
                            if(year) {

                                //if same start months
                                if (startMonthCurr == startMonthCourse && startYearCourse == startYearCurr) {

                                    if((startMonthCurr == endMonthCurr && startYearCurr == endYearCurr) || (startMonthCurr == endMonthCourse && startYearCurr == endYearCourse)) {
                                        //if the start and end months are the same
                                        if (startMonthCurr == endMonthCurr && startYearCurr == endYearCurr) {

                                            System.out.println("Same start and end month");


                                            //if one ends after the other one starts
                                            if (endDayCurr > startDayCourse) {

                                                System.out.println(endDayCurr);
                                                System.out.println(startDayCourse);
                                                return null;
                                            }
                                        }

                                        //if the start and end months are the same
                                        if (startMonthCurr == endMonthCourse && startYearCurr == endYearCourse) {

                                            System.out.println("StartTime idk the difference tbh");

                                            //if the end day overlaps with the other courses start day
                                            if (endDayCourse > startDayCurr) {
                                                return null;
                                            }
                                        }
                                    }

                                    //else it is not valid
                                    else{
                                        return null;
                                    }

                                }

                                //if one starts and ends on the same month the days must not overlap
                                if(startMonthCurr == endMonthCourse && startYearCurr == endYearCourse){

                                    //if it overlaps
                                    if(endDayCourse >= startDayCurr){
                                        return null;
                                    }

                                }
                                //same but the opposite
                                if(startMonthCourse == endMonthCurr && startYearCourse == endYearCourse){
                                    if(endDayCurr >= startDayCourse){
                                        return null;
                                    }
                                }

                                //if the years change must go through 2 loops
                                if(endYearCurr > startYearCurr){

                                    for(int b = startMonthCurr; b <= 12; b++){

                                        if(endYearCourse > startYearCourse){

                                            for(int d = startMonthCourse; d <= 12; d++){

                                                if(b == d && startYearCurr == startYearCourse){
                                                    return null;
                                                }

                                            }

                                            for(int d = 0; d <= endMonthCourse; d++){

                                                if(b == d && startYearCurr == endYearCourse){
                                                    return null;
                                                }

                                            }
                                        }

                                        else{
                                            for(int d = startMonthCourse; d <= endMonthCourse; d++){

                                                if(b == d && startYearCurr == endYearCourse){
                                                    return null;
                                                }

                                            }
                                        }

                                    }

                                    for(int b = 0; b <= endMonthCurr; b++){

                                        if(endYearCourse > startYearCourse){

                                            for(int d = startMonthCourse; d <= 12; d++){

                                                if(b == d && endYearCurr == startYearCourse){
                                                    return null;
                                                }

                                            }

                                            for(int d = 0; d <= endMonthCourse; d++){

                                                if(b == d && endYearCurr == endYearCourse){
                                                    return null;
                                                }

                                            }
                                        }

                                        else{
                                            for(int d = startMonthCourse; d <= endMonthCourse; d++){

                                                if(b == d && endYearCurr == endYearCourse){
                                                    return null;
                                                }

                                            }
                                        }

                                    }

                                }

                                else{

                                    for(int b = startMonthCurr; b <= endMonthCurr; b++){

                                        if(endYearCourse > startYearCourse){

                                            for(int d = startMonthCourse; d <= 12; d++){

                                                System.out.println("In loop loop");

                                                if(b == d && startYearCurr == startYearCourse){
                                                    return null;
                                                }

                                            }

                                            for(int d = 0; d <= endMonthCourse; d++){

                                                System.out.println("Loop loop #2");

                                                if(b == d && startYearCurr == endYearCourse){
                                                    return null;
                                                }

                                            }
                                        }

                                        else{
                                            for(int d = startMonthCourse; d <= endMonthCourse; d++){

                                                if(b == d && startYearCurr == endYearCourse){
                                                    return null;
                                                }

                                            }
                                        }

                                    }
                                }

                            }

                        }

                    }

                }

            }

        }

        //if it is not found it is not a course
        if(!found) {

            courseRepository.save(course);

        }

        //assigns the course to a user
        userController.assignCourseToUser(userId, course.getId());

        //user is updated and current courses are returned
        userRepository.save(u);

        return course;
    }

    /**
     * changes some course information based on the course id
     * @param id
     * @param request
     * @return the changed course
     */
    @ApiOperation(value = "Change Course",notes= "changes anything about the course that is indicated", response = Course.class, tags = "Courses")
    @PutMapping(path = "/course/{id}")
    Course updateCourse(@PathVariable ("id")
                        @ApiParam(name = "id", value = "course id", example = "1") int id,
                        @ApiParam(name = "course info", value = "Course name, start time, end time, etc. \r\n Example {\"course\": \"Course 101\",\"startTime\": 8.0,\"endTime\": 10.0,\"credits\": 4,\"location\": \"Hall\",\"days\": \"T R\"}") @RequestBody Course request){

        //finds which course
        Course course = courseRepository.findById(id);

        //if there is no course at that id return nothing
        if(course == null) {
            return null;
        }

        //editing things if needed
        if(request.getEndTime() != 0.0){
            course.setEndTime(request.getEndTime());
        }
        if(request.getStartTime() != 0.0){
            course.setStartTime(request.getStartTime());
        }
        if(request.getCourse() != null){
            course.setCourse(request.getCourse());
        }
        if(request.getCredits() != 0){
            course.setCredits(request.getCredits());
        }
        if(request.getDays() != null){
            course.setDays(request.getDays());
        }
        if(request.getLocation() != null){
            course.setLocation(request.getLocation());
        }

        //saves the changes and returns that course
        courseRepository.save(course);
        return courseRepository.findById(id);
    }

    /**
     * deletes a course completely from the database
     * @param id
     * @return whether it was successful
     */
    @ApiOperation(value = "Deletes the given course", response = String.class, tags = "Courses")
    @DeleteMapping(path = "/course/delete/{id}")
    String deleteCourse(@PathVariable ("id")
                        @ApiParam(name = "id", value = "course id", example = "1") int id){

        //finds the course
        Course name = courseRepository.findById(id);

        //if not a course at that id it fails
        if(name == null) {
            return failure;
        }

        //the users attached to the course
        List<User> users = name.getUsers();

        //looking through all the users
        for(int i = 0; i < users.size(); i++){

            User u = users.get(i);
            List<Course> courses = u.getCourses();

            //finding this course within the user
            for(int j = 0; j < courses.size(); j ++){

                //if this is the course delete it from the user
                if(courses.get(j).getId() == id){
                    courses.remove(j);
                    userRepository.save(u);
                }
            }

        }

        courseRepository.delete(name);

        return success;

    }

    /**
     * finds thing about the course based on its name
     * @param course
     * @return course
     */
    @ApiOperation(value = "Returns information about the couse based on the course name", response = Course.class, tags = "Courses")
    @GetMapping("/course/info")
    Course courseInfo(@RequestParam String course){

        //test is all courses for the for loop
        List<Course> test = courseRepository.findAll();


        //goes through and checks every course
        for( int i = 1; i <= test.size(); i++){

            //sets a singular course given the id
            Course find = courseRepository.findById(i);

            //if this is the correct course it is returned
            if(find.getCourse().equals(course)){

                return find;

            }
        }

        //returns null if it is not a course in the list
        return null;

    }
}

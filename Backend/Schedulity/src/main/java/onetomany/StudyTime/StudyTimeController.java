package onetomany.StudyTime;

import java.util.List;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import onetomany.Users.User;
import onetomany.Users.UserRepository;
import onetomany.Users.UserController;
import onetomany.Courses.Course;
import onetomany.Courses.CourseController;
import onetomany.Courses.CourseRepository;

@Api(value = "Study Time Controller", description = "All Sleep Time type functions!")
@RestController
public class StudyTimeController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseController courseController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserController userController;

    @Autowired
    StudyTimeRepository studyTimeRepository;

    /**
     * adds the study times and importance of all the users courses
     *
     * @param userCourses
     * @return all the users studytimes
     */
    @PostMapping("/studyTime/{userId}")
    List<StudyTime> addTime(@PathVariable int userId, @RequestBody List<Course> userCourses){

        //number of courses
        int num = userCourses.size();

        //initializing the importance
        int importance = num;

        //the add on if there is 4 credits vs 3
        int addOn = 0;

        //the user
        User user = userRepository.findById(userId);

        //All the users study times
        List<StudyTime> studyTimes = usersStudyTimes(userId);

        //see if it has all courses
        int hasCourse = 0;

        List<Course> uCourses = user.getCourses();

        //making sure these courses are all in the users courses
        for(int i = 0; i < num; i++){

            for(int j = 0; j < uCourses.size(); j++){

                //checks if this course is in there
                if(uCourses.get(j).getId() == userCourses.get(i).getId()){
                    hasCourse += 1;
                }

            }
        }

        if(hasCourse != num){
            return studyTimes;
        }

        //if the user has studyTimes it removes them
        for(int i = studyTimes.size() -1; i >= 0; i--){
                deletesTime(userId, studyTimes.get(i).getCourseId());
        }

        //going through all the courses and setting there study times
        for(int i = 0; i < num; i++){

            //current course
            Course course = userCourses.get(i);

            //initializes addOn
            addOn = 0;

            //if the course is 4 credits the level of importance gets an add on
            if(course.getCredits() == 4){
                addOn = 1;
            }

            //sets the new studytime and saves it
            StudyTime curr = new StudyTime(course.getId(), course.getCourse(), importance+addOn, 0, course.getCredits());

            //sets the current user to it
            curr.setUser(user);

            studyTimeRepository.save(curr);

            userController.assignSTToUser(userId,curr.getId());

            studyTimeRepository.save(curr);

            //subtracts from importance based on what order they were ranked in
            importance--;

        }

        if(user.getStHours() != 0){
            calcTime(userId, user.getStHours());
        }

        return user.getStudyTimes();


    }

    /**
     * calculate time for each course based on total hours
     *
     * @param userId
     * @param hours
     * @return List of all study times for that loser
     */
    @PostMapping("/studyTime/{userId}/{hours}")
    List<StudyTime> calcTime(@PathVariable int userId, @PathVariable double hours){

        //the user
        User user = userRepository.findById(userId);

        //to set a limit to i
        List<StudyTime> All = user.getStudyTimes();

        //the importance of all the persons classes added up
        int total = 0;

        for(int i = 0; i < All.size(); i++){

            //finding the total
            total += All.get(i).getImportance();
        }

        for(int i = 0; i < All.size(); i++){

            StudyTime choice = All.get(i);

            double tot = (double) total;

            //course importance minus total importance is percent to pay attention
            // compared to all other classes
            double temp = choice.getImportance() / tot;

            //percent times total hours will show this classes time per week
            temp = temp * hours;

            choice.setTime(temp);

            user.setStHours(hours);

            studyTimeRepository.save(choice);

            userRepository.save(user);

        }

        return user.getStudyTimes();
    }

    /**
     * returns all the studyTimes in the table
     *
     * @return
     */
    @GetMapping("/studyTime/get")
    List <StudyTime> allStudyTimes(){

        return studyTimeRepository.findAll();

    }

    /**
     * returns all the users study times
     *
     * @param userId
     * @return study times
     */
    @GetMapping("/studyTime/get/{userId}")
    List<StudyTime> usersStudyTimes(@PathVariable int userId){

        return userRepository.findById(userId).getStudyTimes();

    }

    /**
     * adjusts the importance of all the classes
     *
     * @param userId
     * @param sTime
     * @return List of all study times adjusted as seen
     */
    @PutMapping("/studyTime/update/{userId}")
    StudyTime changeStudyTime(@PathVariable int userId, @RequestBody StudyTime sTime){

        User user = userRepository.findById(userId);
        List<StudyTime> studyTimes = user.getStudyTimes();
        StudyTime edit = null;

        if(edit == null){
            return null;
        }

        int pastCred = edit.getCredits();

        //sets the credits in case it has changed
        edit.setCredits(sTime.getCredits());

        //add if more credits
        if(pastCred < edit.getCredits()){
            edit.setImportance(edit.getImportance() + 1);
            calcTime(userId, user.getStHours());
        }
        if(pastCred > edit.getCredits()){
            edit.setImportance(edit.getImportance() - 1);
            calcTime(userId, user.getStHours());
        }

        studyTimeRepository.save(edit);

        return edit;

    }

    /**
     * gets the user and course id to delete that course from the study time and
     * recalculate the study times based on the hours
     *
     * @param userId
     * @param cId
     * @return success or failure
     */
    @DeleteMapping("/studyTime/delete/{userId}/{cId}")
    String deletesTime(@PathVariable int userId, @PathVariable int cId){

        User user = userRepository.findById(userId);
        List <StudyTime> studyTimes = user.getStudyTimes();
        StudyTime delete = null;

        //id in the list
        int id = 0;
        int sTid = -1;

        //looks at all and finds this studyTime
        for(int i = 0; i < studyTimes.size(); i ++){

            //if this is the study time
            if(studyTimes.get(i).getCourseId() == cId){

                //studyTime to delete
                id = i;
                sTid = studyTimes.get(i).getId();
                break;
            }

        }

        //if study time was not in the user
        if(sTid == -1){
            return "Failed, not in this user";
        }

        //removes the study times from the user then saves the user
        user.getStudyTimes().remove(id);
        userRepository.save(user);

        //deletes the study times in general
        studyTimeRepository.deleteById(sTid);

        //recalculates the time
        calcTime(userId, user.getStHours());

        return "Success";

    }

}

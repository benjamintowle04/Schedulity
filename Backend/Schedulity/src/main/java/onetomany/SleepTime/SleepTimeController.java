package onetomany.SleepTime;

import java.util.List;

import io.swagger.annotations.Api;
import onetomany.StudyTime.StudyTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import onetomany.Users.User;
import onetomany.Users.UserRepository;
import onetomany.Users.UserController;

@Api(value = "Sleep Time Controller", description = "All Sleep Time type functions!")
@RestController
public class SleepTimeController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserController userController;

    @Autowired
    SleepTimeRepository sleepTimeRepository;

    /**
     * Adds sleep time info to table
     * Calculates the end time
     * Connects sleep time to the user
     *
     * @param userId
     * @param sTime
     * @return the users sleepTime
     */
    @PostMapping("/sleepTime/{userId}")
    public SleepTime addSleepTime(@PathVariable int userId,@RequestBody SleepTime sTime){

        //finding the user object
        User user = userRepository.findById(userId);

        //if it is null do not add anything
        if(sTime == null){
            return null;
        }

        sTime.setUser(user);

        //saves this sleep time
        sleepTimeRepository.save(sTime);

        setTime(sTime);

        //saves the new changes
        sleepTimeRepository.save(sTime);

        //if user already has sleep time
        if(user.getSleepTime() != null){

            //set that sleep time to null
            user.getSleepTime().setUser(null);

        }

        //setting the sleep time to this user
        user.setSleepTime(sTime);

        //saves the returns the users sleep times
        userRepository.save(user);
        return user.getSleepTime();

    }

    /**
     * Finds the users sleeptime
     *
     * @param userId
     * @return sleepTime
     */
    @GetMapping("/sleepTime/get/{userId}")
    public SleepTime userSleepTime(@PathVariable int userId){
        return userRepository.findById(userId).getSleepTime();
    }

    /**
     * all the sleepTimes are returned
     *
     * @return all SleepTimes
     */
    @GetMapping("/sleepTime/get")
    public List<SleepTime> allSleepTime(){
        return sleepTimeRepository.findAll();
    }

    /**
     * Changes the users sleepTime information
     *
     * @param userId
     * @param sTime
     * @return sleepTime
     */
    @PutMapping("/sleepTime/upDate/{userId}")
    public SleepTime updateTime(@PathVariable int userId, @RequestBody SleepTime sTime){

        User user = userRepository.findById(userId);

        SleepTime oldTime = user.getSleepTime();

        oldTime.setWeekdayStartTime(sTime.getWeekdayStartTime());

        oldTime.setWeekendStartTime(sTime.getWeekendStartTime());

        oldTime.setWeekdayHours(sTime.getWeekdayHours());

        oldTime.setWeekendHours(sTime.getWeekendHours());

        sleepTimeRepository.save(oldTime);

        setTime(oldTime);

        sleepTimeRepository.save(oldTime);

        return oldTime;
    }

    /**
     * sets the endtimes
     *
     * @param sTime
     * @return null
     */
   @PutMapping ("/endTimes")
    void setTime(@RequestBody SleepTime sTime){

        //intializes endTime for the weekdays
        double endTime = sTime.getWeekdayHours() + sTime.getWeekdayStartTime();

        //find the end time
        if(endTime >= 24.0){

            endTime = endTime - 24.0;
        }

        //sets the correct weekday end time
        sTime.setWeekdayEndTime(endTime);

        //intializes endTime for the weekend
        endTime = sTime.getWeekendHours() + sTime.getWeekendStartTime();

        //find the end time
        if(endTime >= 24.0){

            endTime = endTime - 24.0;
        }

        //sets the correct weekend end time
        sTime.setWeekendEndTime(endTime);

    }

    @DeleteMapping("/sleepTime/delete/{sTimeId}")
    public String deleteSTime(@PathVariable int sTimeId){

        SleepTime sTime = sleepTimeRepository.findById(sTimeId);

        if(sTime == null){

            return "Failure, this sleep time does not exist";

        }
        //if has user
        if(sTime.getUser() != null){

            //get the user
            User user = sTime.getUser();

            //resets sleep time
            user.setSleepTime(null);

            userRepository.save(user);

        }

        sleepTimeRepository.deleteById(sTimeId);

        return "Success";

    }

}

package onetomany.Activity;

import java.util.ArrayList;
import java.util.List;

import onetomany.Courses.Course;
import io.swagger.annotations.Api;
import onetomany.StudyTime.StudyTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import onetomany.Users.User;
import onetomany.Users.UserRepository;
import onetomany.Users.UserController;
import onetomany.Users.Event;

import onetomany.SleepTime.SleepTime;
import onetomany.SleepTime.SleepTimeController;
import onetomany.SleepTime.SleepTimeRepository;

@Api(value = "Activity Controller", description = "All Activity type functions!")
@RestController
@Transactional
public class ActivityController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    UserController userController;

    @Autowired
    ActivityRepository activityRepo;

    @Autowired
    SleepTimeController sTimeController;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @PostMapping("/activity/{userId}")
    String addActivity(@PathVariable int userId, @RequestBody Activity activity){

        //if no activity return nothing
        if(activity == null){
            return null;
        }

        //finding the user for this activity
        User u = userRepository.findById(userId);

        String check = checkAct(userId, activity);

        if(check.equals(failure)){
            return check;
        }

        activity.setUser(u);

        //saving activity to table
        activityRepo.save(activity);

        //setting activity to the user and saving it
        u.setActivities(activity);
        userRepository.save(u);

        return success;

    }

    public String checkAct(int userId, Activity activity){

        //finding the user for this activity
        User u = userRepository.findById(userId);

        String[] actDate = activity.getDate().split("-");
        int actMonth = Integer.parseInt(actDate[0]);
        int actDay = Integer.parseInt(actDate[1]);
        int actYear = Integer.parseInt(actDate[2]);
        char day = activity.getDay().charAt(0);

        int aStart = (int)(activity.getStartTime() *100);
        int aEnd = (int)(activity.getEndTime() * 100);

        List<Event> daysEvents = userController.getDay(userId, actMonth,actDay, day, actYear);

        for(int i = 0; i < daysEvents.size(); i++){

            Event e = daysEvents.get(i);

            if(e.geteType().equals("course") || e.geteType().equals("sleep")){

                int eStart = (int)(e.getStart() * 100);
                int eEnd = (int)(e.getEnd() *100);

                for(int j = eStart; j <= eEnd; j++){

                    for(int k = aStart; k <= aEnd; k++){

                        if(k == j){
                            return failure;
                        }

                    }

                }

            }


        }
        return success;

    }

    /**
     * Finds and returns all the users activities
     *
     * @param userId
     * @return users activities
     */
    @GetMapping("/activity/get/{userId}")
    List<Activity> usersActivities(@PathVariable int userId){

        User user = userRepository.findById(userId);
        return user.getActivities();
    }

    /**
     * returns info about a certain activity of the user
     *
     * @param userId
     * @param name
     * @return activity info
     */
    @GetMapping("/activity/get/{userId}/{name}")
    Activity searchByName(@PathVariable int userId, @PathVariable String name){

        User user = userRepository.findById(userId);

        //gets the list of that users activities
        List<Activity> All = user.getActivities();

        //value returned
        Activity activity = null;

        //searches through the activities
        if(All.size() != 0) {

            for (int i = 0; i < All.size(); i++) {

                //this activity info
                activity = All.get(i);

                //if it matches set this as the activity to return
                if(activity.getActName().equals(name)){
                    break;
                }
                else{
                    activity = null;
                }
            }
        }

        return activity;
    }

    /**
     * returns all of the activities in the table
     *
     * @return Activities
     */
    @GetMapping("/activities")
    List <Activity> allActivities(){
        return activityRepo.findAll();
    }

    /**
     * updates something about the activiity object that is input
     *
     * @param userId
     * @param activity
     * @return updated activity
     */
    @PutMapping("/activity/update/{userId}/{actId}")
    String updateActivity(@PathVariable int userId, @PathVariable int actId, @RequestBody Activity activity){

        //find the activity being named
        Activity oldActivity = activityRepo.findById(actId);

        if(oldActivity == null){
            return failure;
        }

        double oldStart = oldActivity.getStartTime();
        double oldEnd = oldActivity.getEndTime();

        //updating activity to be changed
        if(0.0 < activity.getEndTime() && 24.0 >= activity.getEndTime()){
            oldActivity.setEndTime(activity.getEndTime());
        }
        if(0.0 < activity.getStartTime() && 24.0 > activity.getStartTime()){
            oldActivity.setStartTime(activity.getStartTime());
        }

        activityRepo.save(oldActivity);

        String check = checkAct(userId, oldActivity);

        if(check.equals(failure)){

            oldActivity.setStartTime(oldStart);
            oldActivity.setEndTime(oldEnd);

            activityRepo.save(oldActivity);

            return failure;
        }

        return success;
    }

    /**
     * Changes the start and end times for a group of activities
     *
     * @param groupId
     * @param activity
     * @return success or failure
     */
    @PutMapping("/activity/group/{userId}/{groupId}")
    String updateGroupActivity(@PathVariable int userId, @PathVariable int groupId, @RequestBody Activity activity){

        List <Activity> acts = activityRepo.findAll();
        List <Activity> changed = new ArrayList<>();

        boolean found = false;

        for(int i = 0; i < acts.size(); i++){

            Activity time = acts.get(i);

            if(time.getGroupId() == groupId && time.getRecurring() == true){

                double origStart = time.getStartTime();
                double origEnd = time.getEndTime();

                time.setStartTime(activity.getStartTime());
                time.setEndTime(activity.getEndTime());

                activityRepo.save(time);

                changed.add(time);

                String check = checkAct(userId, time);

                if(check.equals(failure)){

                    for(int j = 0; j < changed.size(); j++){

                        Activity act = changed.get(j);

                        time.setStartTime(origStart);
                        time.setEndTime(origEnd);

                        activityRepo.save(time);

                    }
                    return failure;
                }

                found = true;

            }

        }

        if(!found){

            return failure;

        }

        return success;
    }

    /**
     * Deletes the activity from everywhere
     *
     * @param userId
     * @param actId
     * @return Success or Failure
     */
    @DeleteMapping("/activity/delete/{userId}/{actId}")
    public String deleteActivity(@PathVariable int userId, @PathVariable int actId){

        //finding the user
        User user = userRepository.findById(userId);
        Activity act = activityRepo.findById(actId);

        //all the users activities
        List<Activity> All = user.getActivities();

        int id = -1;

        //finds the activity if it is there
        for(int i = 0; i < All.size(); i++){

            if(All.get(i).getId() == actId){
                id = i;
                break;
            }
        }

        if(id == -1){
            return "{\"message\":\"failure\"}";
        }

        else{


            String a = userController.deleteActFromUser(userId, id);
            act.setUser(null);
            //delete the activity from table
            activityRepo.deleteById(actId);


            return a + "TWO";
        }


    }

    /**
     * Deletes one re-ccuring activity
     *
     * @param userId
     * @param groupId
     * @return success or failure
     */
    @DeleteMapping("/activity/delete/group/{userId}/{groupId}")
    public String deleteGroup(@PathVariable int userId, @PathVariable int groupId){

        List<Activity> acts = activityRepo.findAll();

        boolean found = false;

        for(int i = 0; i < acts.size(); i++){

            Activity a = acts.get(i);

            if(a.getGroupId() == groupId && a.getRecurring() ==true){

                deleteActivity(userId, a.getId());
                found = true;

            }

        }

        if(!found){
            return failure;
        }

        return success;

    }


}

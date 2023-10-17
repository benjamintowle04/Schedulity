package onetomany.Advisor;

import onetomany.Courses.Course;
import onetomany.Courses.CourseRepository;
import onetomany.Parents.Parent;
import onetomany.Parents.ParentRepository;
import onetomany.Users.User;
import onetomany.Users.UserController;
import onetomany.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdvisorController {

    @Autowired
    AdvisorRepository advisorRepository;

    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ParentRepository parentRepository;



    //for when code succeeds
    private String success = "{\"message\":\"success\"}";
    //for when code fails
    private String failure = "{\"message\":\"failure\"}";

    private String successID = "{\"message\":\"success\"," + "\n" + "\"id\":";

    /**
     *  lists all users
     * @return list of all parents
     */
    @GetMapping(path = "/advisors")
    List<Advisor> getAllUsers(){
        return advisorRepository.findAll();
    }

    /**
     * finds the given user by their id
     * @param id
     * @return user with id
     */
    @GetMapping(path = "/advisors/{id}")
    Advisor getUserById( @PathVariable int id){
        return advisorRepository.findById(id);
    }

    @PostMapping(path = "/advisors/register")
    String createAdvisor(@RequestBody Advisor advisor){

        List<Advisor> AList = advisorRepository.findAll();
        List<Parent> PList = parentRepository.findAll();
        List<User> UList = userRepository.findAll();
        Advisor a;
        User u;
        Parent p;

        if ((advisor.getEmail() == null) | (advisor.getPassword() == null) | (advisor.getUsername() == null))
        {
            return failure + " bad request";
        }

        for (Advisor value : AList) {
            a = value;
            if (advisor.getUsername().equals(a.getUsername()) | advisor.getEmail().equals(a.getEmail())) {
                return failure + " Username or Email already in use";
            }
        }

        for (User value : UList) {
            u = value;
            if (advisor.getUsername().equals(u.getUsername()) || advisor.getEmail().equals(u.getEmail())) {
                return failure + " Username or Email already in use";
            }
        }

        for (Parent value : PList) {
            p = value;
            if (advisor.getUsername().equals(p.getUsername()) || advisor.getEmail().equals(p.getEmail())) {
                return failure + " Username or Email already in use";
            }
        }

        advisorRepository.save(advisor);
        return successID + advisor.getId() + "}";
    }

    @PostMapping(path = "/advisors/login")
    String login(@RequestBody Advisor advisor)
    {

        if ((advisor.getEmail() == null) | (advisor.getPassword() == null) | (advisor.getUsername() == null))
        {
            return failure + "null entries";
        }
        else if(findByUsername(advisor.getUsername()).equals(null))
        {
            return failure + "username not correct";
        }
        Advisor test = findByUsername(advisor.getUsername());
        if(test.getPassword().equals(advisor.getPassword()) & test.getEmail().equals(advisor.getEmail()))
        {
            return successID + test.getId() + "}";
        }
        else{ return failure + "Email or Password Incorrect"; }
    }

    @GetMapping("advisors/find/{uName}")
    public Advisor findByUsername(@PathVariable String uName)
    {
        List<Advisor> AList = advisorRepository.findAll();
        for(int i = 0; i < AList.size();i++)
        {
            if(AList.get(i).getUsername().equals(uName))
            {
                return AList.get(i);
            }
        }
        return null;
    }

    @PostMapping("advisors/addStudent/{id}/{Sid}")
    public String addStudent(@PathVariable int id, @PathVariable int Sid)
    {
        User user = userRepository.findById(Sid);
        Advisor advisor = advisorRepository.findById(id);

        List<User> SList = advisor.getStudent();

        for(int i = 0; i < SList.size(); i++)
        {
            if(SList.get(i).getId() == Sid)
            {
                return failure + "Already Student";
            }
        }

        advisor.getStudent().add(user);
        advisor.getStudents().add(user.getUsername());
        user.setAdvisor(advisor);
        user.setAdvisorName(advisor.getUsername());
        userRepository.save(user);
        advisor.getStudent().remove(advisor.getStudents().size() - 1);
        advisor.getStudent().add(user);
        advisorRepository.save(advisor);

        return success;
    }

    @DeleteMapping("advisors/dropStudent/{id}/{Sid}")
    public String dropStudent(@PathVariable int id, @PathVariable int Sid)
    {
        Advisor advisor = advisorRepository.findById(id);
        List<User> Slist = advisor.getStudent();

        if(Slist.size() == 0)
        {
            return failure + " no student to drop";
        }
        else
        {
            for(int i =0; i < Slist.size(); i++)
            {
                if(Slist.get(i).getId() == Sid)
                {
                    advisor.getStudent().remove(i);
                    advisor.getStudents().remove(i);
                    advisorRepository.save(advisor);
                    User user = userRepository.findById(Sid);
                    user.setAdvisor(null);
                    user.setAdvisorName("");
                    userRepository.save(user);
                    return success;
                }
            }
            return failure + "no student with id";
        }
    }

    @PutMapping("advisors/dropCourse/{id}/{Sid}/{Cid}")
    public String dropCourse(@PathVariable int id, @PathVariable int Sid, @PathVariable int Cid)
    {
        Advisor advisor = advisorRepository.findById(id);
        User user = userRepository.findById(Sid);
        Course course = courseRepository.findById(Cid);

        if(user.getAdvisorName().equals(advisor.getUsername()))
        {
            List <Course> CList = user.getCourses();
            for(int i = 0; i < CList.size();i++)
            {
                if(CList.get(i).getId() == Cid)
                {
                    user.getCourses().remove(i);
                    userRepository.save(user);
                    List<User> UList = course.getUsers();
                    for(int j = 0; j < UList.size();j++)
                    {
                        if(UList.get(j).getId() == Sid)
                        {
                            course.getUsers().remove(j);
                            courseRepository.save(course);
                                    return success;
                        }
                    }
                }
            }
            return failure;
        }
        else
        {
            return failure;
        }
    }

    @PutMapping("advisors/changeSection/{id}/{Sid}/{Cid}")
    public String changeSection(@PathVariable int id, @PathVariable int Sid, @PathVariable int Cid, @RequestBody Course c)
    {
        Advisor advisor = advisorRepository.findById(id);
        User user = userRepository.findById(Sid);
        Course course = courseRepository.findById(Cid);

        if(user.getAdvisorName().equals(advisor.getUsername()))
        {
            List <Course> CList = user.getCourses();
            for(int i = 0; i < CList.size();i++)
            {
                if(CList.get(i).getId() == Cid)
                {
                    course.setStartTime(c.getStartTime());
                    course.setDays(c.getDays());
                    course.setEndTime(c.getEndTime());
                    course.setLocation(c.getLocation());

                    courseRepository.save(course);
                    return success;
                }
            }
            return failure;
        }
        else
        {
            return failure;
        }
    }

    //retire this is frustrating
    @DeleteMapping("advisors/retire/{id}")
    public String retire(@PathVariable int id)
    {
        Advisor advisor = advisorRepository.findById(id);
        List<User> SList = advisor.getStudent();
        User user;
        if(SList.size() == 0)
        {
            advisorRepository.deleteById(id);
            return success;
        }
        else
        {
            for (int i = SList.size() - 1; i >= 0;i--)
            {
                dropStudent(id,SList.get(i).getId());
//                user = SList.get(i);
//                user.setAdvisor(null);
//                user.setAdvisorName("");
//                userRepository.save(user);

            }
            advisorRepository.deleteById(id);
            return success;
        }

    }
}

package onetomany.Parents;

import onetomany.Advisor.Advisor;
import onetomany.Advisor.AdvisorRepository;
import onetomany.Courses.Course;
import onetomany.Courses.CourseRepository;
import onetomany.StudyTime.StudyTimeRepository;
import onetomany.Users.User;
import onetomany.Users.UserController;
import onetomany.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ParentController {

    @Autowired
    ParentRepository parentRepository;

    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdvisorRepository advisorRepository;


    //for when code succeeds
    private String success = "{\"message\":\"success\"}";
    //for when code fails
    private String failure = "{\"message\":\"failure\"}";

    private String successID = "{\"message\":\"success\"," + "\n" + "\"id\":";

    /**
     *  lists all users
     * @return list of all parents
     */
    @GetMapping(path = "/parents")
    List<Parent> getAllUsers(){
        return parentRepository.findAll();
    }

    /**
     * finds the given user by their id
     * @param id
     * @return user with id
     */
    @GetMapping(path = "/parents/{id}")
    Parent getUserById( @PathVariable int id){
        return parentRepository.findById(id);
    }

    @PostMapping(path = "/parents/register")
    String createParent(@RequestBody Parent parent){

        List<Parent> PList = parentRepository.findAll();
        List<Advisor> AList = advisorRepository.findAll();
        List<User> UList = userRepository.findAll();
        Parent p;
        Advisor a;
        User u;

        if ((parent.getEmail() == null) || (parent.getPassword() == null) || (parent.getUsername() == null))
        {
            return failure + " bad request";
        }

        for (Parent value : PList) {
            p = value;
            if (parent.getUsername().equals(p.getUsername()) || parent.getEmail().equals(p.getEmail())) {
                return failure + " Username or Email already in use";
            }
        }

        for (Advisor value : AList) {
            a = value;
            if (parent.getUsername().equals(a.getUsername()) || parent.getEmail().equals(a.getEmail())) {
                return failure + " Username or Email already in use";
            }
        }

        for (User value : UList) {
            u = value;
            if (parent.getUsername().equals(u.getUsername()) || parent.getEmail().equals(u.getEmail())) {
                return failure + " Username or Email already in use";
            }
        }

        parentRepository.save(parent);
        return successID + parent.getId() + "}";
    }

    @PostMapping(path = "/parents/login")
    String login(@RequestBody Parent parent)
    {

        if ((parent.getEmail() == null) | (parent.getPassword() == null) | (parent.getUsername() == null))
        {
            return failure + "null entries";
        }
        else if(findByUsername(parent.getUsername()).equals(null))
        {
            return failure + "username not correct";
        }
        Parent test = findByUsername(parent.getUsername());
        if(test.getPassword().equals(parent.getPassword()) & test.getEmail().equals(parent.getEmail()))
        {
            return successID + test.getId() + "}";
        }
        else{ return failure + "Email or Password Incorrect"; }
    }

    @GetMapping("parents/find/{uName}")
    public Parent findByUsername(@PathVariable String uName)
    {
        List<Parent> UList = parentRepository.findAll();
        for(int i = 0; i < UList.size();i++)
        {
            if(UList.get(i).getUsername().equals(uName))
            {
                return UList.get(i);
            }
        }
        return null;
    }

    @PostMapping("parent/request/{id}/{Cid}")
    public String SendRequest(@PathVariable int id, @PathVariable int Cid)
    {
        Parent parent = parentRepository.findById(id);
        User user = userRepository.findById(Cid);

        if(parent.equals(null) || user.equals(null))
        {
            return failure + "Null parent or user";
        }
//        else if(parent.getChild().equals(null))
//        {
//
//        }
//        else if(parent.getChild().equals(user))
//        {
//            return failure + "already your child";
//        }
        user.setParentReq(parent.getId());
        parent.setRequest(user.getId());
        userRepository.save(user);
        parentRepository.save(parent);

        return success;
    }

    @GetMapping("parents/getSched/{id}")
    public List<Course> getSched(@PathVariable int id)
    {
        Parent parent = parentRepository.findById(id);
        if(parent.equals(null))
        {
            return null;
        }
        else if(parent.getChild().equals(null))
        {
            return null;
        }

        return parent.getChild().getCourses();

    }

    @DeleteMapping("parents/{id}/delete")
    public void delete(@PathVariable int id)
    {
        Parent parent = parentRepository.findById(id);
        if(parent.getChildName().equals(""))
        {
            parentRepository.deleteById(id);
        }
        else
        {
            User user = parent.getChild();
            parent.setChildName("");
            parent.setChild(null);
            user.setParentName("");
            user.setParent(null);
            user.setParentReq(0);
            userRepository.save(user);
            parentRepository.deleteById(id);
        }
    }

    @PutMapping("parents/{id}/putUpForAdoption")
    public void removeChild(@PathVariable int id)
    {
        Parent parent = parentRepository.findById(id);

        if(parent.getChildName().equals(""))
        {
            parent.setRequest(0);
        }
        else
        {
            User user = parent.getChild();
            parent.setChild(null);
            parent.setChildName("");
            parent.setRequest(0);
            user.setParent(null);
            user.setParentName("");
            user.setParentReq(0);
            parentRepository.save(parent);
            userRepository.save(user);
        }
    }

}

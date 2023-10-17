package onetomany;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetomany.Courses.Course;
import onetomany.Courses.CourseRepository;
import onetomany.Users.User;
import onetomany.Users.UserRepository;
import onetomany.Users.UserController;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@SpringBootApplication
//@EnableJpaRepositories
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    // Create 3 users with their machines
    /**
     * 
     * @param userRepository repository for the User entity
     * @param courseRepository repository for the Course entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */

//    @Bean
//    CommandLineRunner initUser(UserRepository userRepository, CourseRepository courseRepository) {
//        return args -> {
//            User user1 = new User("John", "stamos","User","john@someemail.com");
//            User user2 = new User("Jane", "jane@somemail.com","2","Jane@doe.org");
//            User user3 = new User("Justin", "justin@somemail.com","3", "justin@fortnite.com");
//            Course course1 = new Course("CPR E 288", "Student Innovation Center", "Tuesday and Thursday", 9.30, 10.45, 10, 4, true, 0);
//            Course course2 = new Course( "EE 230", "Marston", "Monday, Wednesday, Friday", 4.25, 5.15, 9, 4, true, 0);
//            Course course3 = new Course( "Com S 309", "Advanced Teaching and Research Building", "Monday, Wednesday, Friday", 3.20, 4.10, 8, 3, false, 0);
//
//            courseRepository.save(course1);
//            courseRepository.save(course2);
//            courseRepository.save(course3);
//            String s;
////            s = userController.assignCourseToUser(1,1);
////            s = userController.assignCourseToUser(2, 2);
////            s = userController.assignCourseToUser(3, 3);
//            userRepository.save(user1);
//            userRepository.save(user2);
//            userRepository.save(user3);
//
//        };
//    }

}

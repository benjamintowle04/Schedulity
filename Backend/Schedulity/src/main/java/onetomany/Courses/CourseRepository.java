package onetomany.Courses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Lizzy Schmitt
 * 
 */ 

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findById(int id);

    @Transactional
    void deleteById(int id);
}

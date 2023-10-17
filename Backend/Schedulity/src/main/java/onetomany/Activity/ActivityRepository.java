package onetomany.Activity;

import onetomany.StudyTime.StudyTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findById(int id);

    @Transactional
    void deleteById(int id);
}



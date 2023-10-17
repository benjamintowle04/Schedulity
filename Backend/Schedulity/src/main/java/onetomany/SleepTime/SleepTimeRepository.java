package onetomany.SleepTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SleepTimeRepository extends JpaRepository<SleepTime, Long> {
    SleepTime findById(int id);

    @Transactional
    void deleteById(int id);
}
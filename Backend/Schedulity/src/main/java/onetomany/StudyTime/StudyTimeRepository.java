package onetomany.StudyTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {
     StudyTime findById(int id);

    @Transactional
    void deleteById(int id);
}

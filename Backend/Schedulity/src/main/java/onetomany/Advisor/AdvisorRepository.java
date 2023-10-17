package onetomany.Advisor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Vivek Bengre
 *
 */

public interface AdvisorRepository extends JpaRepository<Advisor, Long> {

    Advisor findById(int id);

    @Transactional
    void deleteById(int id);
}
package onetomany.Parents;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Vivek Bengre
 *
 */

public interface ParentRepository extends JpaRepository<Parent, Long> {

    Parent findById(int id);

      @Transactional
    void deleteById(int id);
}

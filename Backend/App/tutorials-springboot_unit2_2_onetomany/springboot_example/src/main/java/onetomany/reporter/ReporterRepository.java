package onetomany.reporter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Vikrant Gandotra
 */

@Repository
public interface ReporterRepository extends JpaRepository<Reporter, Long> {
    Reporter findByEmail(String email);
}

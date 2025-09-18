package onetomany.newsusers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Vikrant Gandotra
 */

@Repository
public interface NewsUserRepository extends JpaRepository<NewsUser, Long> {
    NewsUser findByEmail(String email);
    NewsUser findByUsername(String username);

}

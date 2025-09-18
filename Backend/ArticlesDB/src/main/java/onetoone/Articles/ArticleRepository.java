package onetoone.Articles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

/**
 * @author Vikrant Gandotra
 */

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    // Find all articles written by a given author
    List<Article> findByAuthor(String author);

    // Find all articles published by a given publication
    List<Article> findByPublication(String publication);

    // Find an article by its title
    Optional<Article> findByTitle(String title);

    // Find articles published between two dates
    List<Article> findByPublicationDateBetween(LocalDate startDate, LocalDate endDate);

    // Find articles whose title contains a word
    List<Article> findByTitleContaining(String keyword);
}

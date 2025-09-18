package onetoone.Articles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

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
}

package onetoone.Articles;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

/**
 * @author Vikrant Gandotra
 */

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    // Retrieve all articles
    @GetMapping
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    // Retrieve a single article by id
    @GetMapping("/id/{id}")
    public Article getArticleById(@PathVariable Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    // Retrieve all articles by a specific author
    @GetMapping("/author/{author}")
    public List<Article> getArticlesByAuthor(@PathVariable String author) {
        return articleRepository.findByAuthor(author);
    }

    // Retrieve all articles by publication
    @GetMapping("/publication/{publication}")
    public List<Article> getArticlesByPublication(@PathVariable String publication) {
        return articleRepository.findByPublication(publication);
    }

    // Retrieve an article by title
    @GetMapping("/title/{title}")
    public Article getArticleByTitle(@PathVariable String title) {
        Optional<Article> optionalArticle = articleRepository.findByTitle(title);
        return optionalArticle.orElse(null);
    }

    // Create a new article
    @PostMapping
    public Article createArticle(@RequestBody Article article) {
        return articleRepository.save(article);
    }

    // Update an existing article by id
    @PutMapping("/{id}")
    public Article updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return null;
        }
        Article article = optionalArticle.get();
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setAuthor(articleDetails.getAuthor());
        article.setPublication(articleDetails.getPublication());
        article.setPublicationDate(articleDetails.getPublicationDate());
        return articleRepository.save(article);
    }

    // Delete an article by id
    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleRepository.deleteById(id);
        return "{\"message\":\"Article deleted\"}";
    }

    // Filter articles by publication date range
    // URL is http://coms-3090-003.class.las.iastate.edu:8080/articles/filterByDate?start=2025-01-01&end=2025-12-21
    @GetMapping("/filterByDate")
    public List<Article> filterArticlesByDate(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
//        LocalDate startDate = LocalDate.parse(start);
//        LocalDate endDate = LocalDate.parse(end);
        return articleRepository.findByPublicationDateBetween(start, end);
    }

    // Search articles by a word
    // URL is http://coms-3090-003.class.las.iastate.edu:8080/articles/search?keyword=Market
    @GetMapping("/search")
    public List<Article> searchArticlesByTitle(@RequestParam String keyword) {
        return articleRepository.findByTitleContaining(keyword);
    }
}

package onetomany.Articles;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import onetomany.admin.Admin;
import onetomany.admin.AdminRepository;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ApprovedArticleRepository approvedArticleRepository;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        Optional<Article> articleOpt = articleRepository.findById(id);
        return articleOpt.map(article -> new ResponseEntity<>(article, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<Article>> getArticlesByAuthor(@PathVariable String author) {
        List<Article> articles = articleRepository.findByAuthor(author);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/publication/{publication}")
    public ResponseEntity<List<Article>> getArticlesByPublication(@PathVariable String publication) {
        List<Article> articles = articleRepository.findByPublication(publication);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Article> getArticleByTitle(@PathVariable String title) {
        Optional<Article> articleOpt = articleRepository.findByTitle(title);
        return articleOpt.map(article -> new ResponseEntity<>(article, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        // Set defaults for new article
        article.setApproved(false);
        article.setPendingDeletion(false);
        Article savedArticle = articleRepository.save(article);
        return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Article article = optionalArticle.get();
        if (article.isPendingDeletion()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setAuthor(articleDetails.getAuthor());
        article.setPublication(articleDetails.getPublication());
        article.setPublicationDate(articleDetails.getPublicationDate());
        // Mark as unapproved if edited
        article.setApproved(false);
        Article updatedArticle = articleRepository.save(article);
        return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Article> requestDeleteArticle(@PathVariable Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Article article = optionalArticle.get();
        article.setPendingDeletion(true);
        Article updatedArticle = articleRepository.save(article);
        return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveArticle(@PathVariable Long id, @RequestParam(required = false) Long adminId) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Article article = optionalArticle.get();
        article.setApproved(true);
        article.setPendingDeletion(false);

        if (adminId != null) {
            Optional<Admin> adminOpt = adminRepository.findById(adminId);
            if (adminOpt.isPresent()) {
                article.setApprovedBy(adminOpt.get());
            }
        }

        ApprovedArticle approvedArticle = new ApprovedArticle(article);
        approvedArticleRepository.save(approvedArticle);

        articleRepository.delete(article);
        return new ResponseEntity<>(approvedArticle, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/finalizeDeletion")
    public ResponseEntity<String> finalizeDeleteArticle(@PathVariable Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent() || !optionalArticle.get().isPendingDeletion()) {
            return new ResponseEntity<>("{\"message\":\"Article not pending deletion\"}", HttpStatus.BAD_REQUEST);
        }
        articleRepository.deleteById(id);
        return new ResponseEntity<>("{\"message\":\"Article permanently deleted\"}", HttpStatus.OK);
    }

    @GetMapping("/filterByDate")
    public ResponseEntity<List<Article>> filterArticlesByDate(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<Article> articles = articleRepository.findByPublicationDateBetween(start, end);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Article>> searchArticlesByTitle(@RequestParam String keyword) {
        List<Article> articles = articleRepository.findByTitleContaining(keyword);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/reporter/{reporterName}")
    public ResponseEntity<List<Article>> getArticlesByReporter(@PathVariable String reporterName) {
        List<Article> articles = articleRepository.findByReporter_Username(reporterName);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }
}

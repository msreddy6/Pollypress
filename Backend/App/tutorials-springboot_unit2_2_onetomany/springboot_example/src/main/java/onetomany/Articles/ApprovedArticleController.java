package onetomany.Articles;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/approved-articles")
public class ApprovedArticleController {

    @Autowired
    private ApprovedArticleRepository approvedArticleRepository;

    @GetMapping
    public ResponseEntity<List<ApprovedArticle>> getAllApprovedArticles() {
        List<ApprovedArticle> articles = approvedArticleRepository.findAll();
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApprovedArticle> getApprovedArticleById(@PathVariable Long id) {
        Optional<ApprovedArticle> articleOpt = approvedArticleRepository.findById(id);
        return articleOpt.map(article -> new ResponseEntity<>(article, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ApprovedArticle> createApprovedArticle(@RequestBody ApprovedArticle article) {
        ApprovedArticle savedArticle = approvedArticleRepository.save(article);
        return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApprovedArticle> updateApprovedArticle(@PathVariable Long id, @RequestBody ApprovedArticle articleDetails) {
        Optional<ApprovedArticle> optionalArticle = approvedArticleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ApprovedArticle article = optionalArticle.get();
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setAuthor(articleDetails.getAuthor());
        article.setPublication(articleDetails.getPublication());
        article.setPublicationDate(articleDetails.getPublicationDate());
        article.setReporter(articleDetails.getReporter());
        article.setApprovedBy(articleDetails.getApprovedBy());
        ApprovedArticle updatedArticle = approvedArticleRepository.save(article);
        return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApprovedArticle(@PathVariable Long id) {
        if (!approvedArticleRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        approvedArticleRepository.deleteById(id);
        return new ResponseEntity<>("{\"message\":\"Approved article deleted\"}", HttpStatus.OK);
    }
}

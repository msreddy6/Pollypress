package onetomany.Articles;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import onetomany.admin.Admin;
import onetomany.reporter.Reporter;

@Entity
@Table(name = "approved_articles")
public class ApprovedArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String author;
    private String publication;
    private LocalDate publicationDate;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private Reporter reporter;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin approvedBy;

    public ApprovedArticle() {}

    public ApprovedArticle(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getAuthor();
        this.publication = article.getPublication();
        this.publicationDate = article.getPublicationDate();
        this.reporter = article.getReporter();
        this.approvedBy = article.getApprovedBy();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublication() {
        return publication;
    }
    public void setPublication(String publication) {
        this.publication = publication;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }
    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Reporter getReporter() {
        return reporter;
    }
    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    public Admin getApprovedBy() {
        return approvedBy;
    }
    public void setApprovedBy(Admin approvedBy) {
        this.approvedBy = approvedBy;
    }
}

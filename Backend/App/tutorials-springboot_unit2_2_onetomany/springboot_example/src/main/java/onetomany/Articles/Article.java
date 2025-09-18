package onetomany.Articles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // added this line
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import onetomany.reporter.Reporter;
import onetomany.admin.Admin;
import onetomany.newsusers.NewsUser;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Vikrant Gandotra
 */

@Entity
@Table(name = "articles")
public class Article {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;




    private String title;

    private boolean approved;

    private boolean pendingDeletion;

    // The content text body of the article
    private String content;

    private String author;

    // Name of the publication or news outlet
    private String publication;

    // Publication date of the article
    private LocalDate publicationDate;



    @ManyToOne
    @JoinColumn(name = "reporter_id")
    @JsonIgnoreProperties("articles") // added this line
    private Reporter reporter;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnoreProperties("approvedArticles") // added this line
    private Admin approvedBy;

    @ManyToMany(mappedBy = "favoriteArticles")
    private Set<NewsUser> favoritedByUsers = new HashSet<>();



//    public Article(String breakingNews, String contentOfBreakingNews, Reporter reporter1, String globalNews, LocalDate now) {}

    public Article() {}


    public Article(String title, String content, Reporter reporter, String publication, LocalDate publicationDate) {
        this.title = title;
        this.content = content;
        this.reporter = reporter;
        this.publication = publication;
        this.publicationDate = publicationDate;
        this.approved = false;
    }


    public Article(String title, String content, String author, String publication, LocalDate publicationDate) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.publication = publication;
        this.publicationDate = publicationDate;
        this.approved=false;
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

    public boolean isApproved() {
        return approved;
    }

    public boolean isPendingDeletion() {
        return pendingDeletion;
    }

    public void setPendingDeletion(boolean pendingDeletion) {
        this.pendingDeletion = pendingDeletion;
    }

    public void setApprovedBy(Admin approvedBy) {
        this.approvedBy = approvedBy;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}

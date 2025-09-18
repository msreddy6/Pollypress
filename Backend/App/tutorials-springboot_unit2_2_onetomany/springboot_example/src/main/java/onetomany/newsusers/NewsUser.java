package onetomany.newsusers;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import java.util.HashSet;
import java.util.Set;
import onetomany.Articles.Article;

/**
 * @author Vikrant Gandotra
 */

@Entity
@Table(name = "news_users")
public class NewsUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    public NewsUser() {}

    @ManyToMany
    @JoinTable(
            name = "newsuser_favorites",
            joinColumns = @JoinColumn(name = "news_user_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    private Set<Article> favoriteArticles = new HashSet<>();

    public NewsUser(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setName(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


//    public String getPreferredLanguage() {
//        return password;
//    }
//    public void setPreferredLanguage(String preferredLanguage) {
//        this.password = preferredLanguage;
//    }
}

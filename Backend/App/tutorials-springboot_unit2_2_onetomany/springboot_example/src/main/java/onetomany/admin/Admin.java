package onetomany.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.ArrayList;
import onetomany.Articles.Article;

/**
 * @author Vikrant Gandotra
 */

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "approvedBy")
    private List<Article> approvedArticles = new ArrayList<>();

    public Admin() {}

    public Admin(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password=password;
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
    public void setUsername(String name) {
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


    public List<Article> getApprovedArticles() {
        return approvedArticles;
    }
    public void setApprovedArticles(List<Article> approvedArticles) {
        this.approvedArticles = approvedArticles;
    }
}

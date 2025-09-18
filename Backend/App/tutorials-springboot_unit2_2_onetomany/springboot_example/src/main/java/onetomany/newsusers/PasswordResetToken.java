package onetomany.newsusers;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Vikrant Gandotra
 */

@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expiryDate;

    @OneToOne
    @JoinColumn(name = "news_user_id")
    private NewsUser newsUser;

    public PasswordResetToken() {}

    public PasswordResetToken(String token, NewsUser newsUser, LocalDateTime expiryDate) {
        this.token = token;
        this.newsUser = newsUser;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public NewsUser getNewsUser() {
        return newsUser;
    }
    public void setNewsUser(NewsUser newsUser) {
        this.newsUser = newsUser;
    }
}

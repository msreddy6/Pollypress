package onetomany.newsusers;

//import java.util.List;
//import java.util.Optional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import onetomany.security.JwtUtil;

/**
 * @author Vikrant Gandotra
 */

@RestController
@RequestMapping("/newsusers")
public class NewsUserController {

    @Autowired
    private NewsUserRepository newsUserRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    // Retrieve all news users
    @GetMapping
    public List<NewsUser> getAllNewsUsers() {
        return newsUserRepository.findAll();
    }

    // Retrieve a news user by id
    @GetMapping("/{id}")
    public NewsUser getNewsUserById(@PathVariable Long id) {
        Optional<NewsUser> newsUserOpt = newsUserRepository.findById(id);
        return newsUserOpt.orElse(null);
    }

    // Create a new news user
    @PostMapping
    public NewsUser createNewsUser(@RequestBody NewsUser newsUser) {
        return newsUserRepository.save(newsUser);
    }

    // Update an existing news user
    @PutMapping("/{id}")
    public NewsUser updateNewsUser(@PathVariable Long id, @RequestBody NewsUser newsUserDetails) {
        Optional<NewsUser> optionalNewsUser = newsUserRepository.findById(id);
        if (!optionalNewsUser.isPresent()) {
            return null;
        }
        NewsUser newsUser = optionalNewsUser.get();
        newsUser.setName(newsUserDetails.getUsername());
        newsUser.setEmail(newsUserDetails.getEmail());
        newsUser.setPassword(newsUserDetails.getPassword());
        //newsUser.setPreferredLanguage(newsUserDetails.getPreferredLanguage());
        return newsUserRepository.save(newsUser);
    }

    // Delete a news user by id
    @DeleteMapping("/{id}")
    public String deleteNewsUser(@PathVariable Long id) {
        newsUserRepository.deleteById(id);
        return "{\"message\":\"News user deleted\"}";
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        Optional<NewsUser> userOptional = newsUserRepository.findAll().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
        if(!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("{\"message\":\"No user found with that email\"}");
        }
        NewsUser user = userOptional.get();
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);
        PasswordResetToken prt = new PasswordResetToken(token, user, expiryDate);
        tokenRepository.save(prt);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, please click the following link: " +
                "http://localhost:8080/newsusers/resetPassword?token=" + token);
        mailSender.send(mailMessage);
        return ResponseEntity.ok("{\"message\":\"Password reset email sent\"}");
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
        PasswordResetToken prt = tokenRepository.findByToken(token);
        if(prt == null || prt.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("{\"message\":\"Invalid or expired token\"}");
        }
        NewsUser user = prt.getNewsUser();
        user.setPassword(newPassword);
        newsUserRepository.save(user);
        tokenRepository.delete(prt);
        return ResponseEntity.ok("{\"message\":\"Password successfully reset\"}");
    }
}


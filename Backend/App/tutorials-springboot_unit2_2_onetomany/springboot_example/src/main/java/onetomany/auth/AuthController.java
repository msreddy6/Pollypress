package onetomany.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import onetomany.newsusers.LoginRequest;
import onetomany.newsusers.NewsUser;
import onetomany.newsusers.NewsUserRepository;
import onetomany.admin.Admin;
import onetomany.admin.AdminRepository;
import onetomany.reporter.Reporter;
import onetomany.reporter.ReporterRepository;
import onetomany.security.JwtUtil;

/**
 * @author Vikrant Gandotra
 */

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ReporterRepository reporterRepository;
    @Autowired
    private NewsUserRepository newsUserRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String role = loginRequest.getRole();
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        String token = null;

        if(role.equalsIgnoreCase("newsuser")) {
            NewsUser user = newsUserRepository.findByEmail(email);
            if(user == null || !user.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\":\"Invalid credentials for news user\"}");
            }
            token = JwtUtil.generateToken(user.getUsername());
        } else if(role.equalsIgnoreCase("admin")) {
            Admin admin = adminRepository.findByEmail(email);
            if(admin == null || !admin.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\":\"Invalid credentials for admin\"}");
            }
            token = JwtUtil.generateToken(admin.getUsername());
        } else if(role.equalsIgnoreCase("reporter")) {
            Reporter reporter = reporterRepository.findByEmail(email);
            if(reporter == null || !reporter.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\":\"Invalid credentials for reporter\"}");
            }
            token = JwtUtil.generateToken(reporter.getUsername());
        } else {
            return ResponseEntity.badRequest().body("{\"message\":\"Invalid role specified\"}");
        }

        return ResponseEntity.ok("{\"token\":\"" + token + "\"}");
    }
}

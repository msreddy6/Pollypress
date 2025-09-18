package onetomany.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * @author Vikrant Gandotra
 */

public class JwtUtil {
    // 64-bit key to verify ID
    private static final String SECRET_KEY = "mkFxaCCprL7svJUAqpeBYmkgDW7Dq2wxmkFxaCCprL7svJUAqpeBYmkgDW7Dq2wx";

    // Key expiration time limit
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    public static String generateToken(String subject) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}


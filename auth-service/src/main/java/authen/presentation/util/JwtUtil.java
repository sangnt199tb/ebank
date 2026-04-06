package authen.presentation.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.bouncycastle.jcajce.BCFKSLoadStoreParameter;

import java.util.Date;
import java.util.Map;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String SECRET = "K7vN3pL9xM2qR5wT8jY4bC6zF1gH0dJ9sK5mN2bP";

    private final long EXPIRATION = 86400000;

    public String generateToken(String username, String role, String cif) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("cif", cif)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
}

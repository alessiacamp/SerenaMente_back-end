package SerenaMente.progetto.java.Security;

import SerenaMente.progetto.java.Entities.Utente;
import SerenaMente.progetto.java.Entities.Volontario;
import SerenaMente.progetto.java.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWT {
    @Value("${jwt.secret}")
    private String secret;

    public String createToken(Object user) {
        if (!(user instanceof Utente) && !(user instanceof Volontario)) {
            throw new IllegalArgumentException("Il token può essere creato solo per Utente o Volontario");
        }

        String userId = user instanceof Utente
                ? ((Utente) user).getId().toString()
                : ((Volontario) user).getId().toString();

        String role = user instanceof Utente
                ? ((Utente) user).getRuolo().name()
                : "VOLONTARIO";

        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .subject(userId)
                .claim("role", role)
                .claim("userType", user instanceof Utente ? "UTENTE" : "VOLONTARIO")
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public void verifyToken(String accessToken) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build().parse(accessToken);
        } catch (Exception ex) {
            throw new UnauthorizedException("Problemi con il token! Per favore effettua di nuovo il login!");
        }
    }

    public String getIdFromToken(String accessToken) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .getSubject();
    }

    public String getRoleFromToken(String accessToken) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .get("role", String.class);
    }

    public String getUserTypeFromToken(String accessToken) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .get("userType", String.class);
    }
}
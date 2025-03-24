package SerenaMente.progetto.java.Security;

import SerenaMente.progetto.java.Entities.Utente;
import SerenaMente.progetto.java.Services.UtenteService;
import SerenaMente.progetto.java.exceptions.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTCheckerFilter extends OncePerRequestFilter {

    @Autowired
    private JWT jwt;

    @Autowired
    private UtenteService utenteService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        if (request.getRequestURI().equals("/auth/login")  || request.getRequestURI().equals("/utente")|| request.getRequestURI().equals("/volontario"))  {
            filterChain.doFilter(request, response);
            return;
        }


        String authHeader = request.getHeader("Authorization");


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Inserire token nell'Authorization Header nel formato corretto!");
        }


        String accessToken = authHeader.substring(7);


        jwt.verifyToken(accessToken);


        String utenteId = jwt.getIdFromToken(accessToken);
        Utente currentUtente = null;
        try {
            currentUtente = this.utenteService.findById(UUID.fromString(utenteId));
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }


        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUtente, null, currentUtente.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        filterChain.doFilter(request, response);
    }
}
package SerenaMente.progetto.java.Services;

import SerenaMente.progetto.java.Entities.Utente;
import SerenaMente.progetto.java.Entities.Volontario;
import SerenaMente.progetto.java.Security.JWT;
import SerenaMente.progetto.java.exceptions.NotFoundException;
import SerenaMente.progetto.java.exceptions.UnauthorizedException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UtenteService utenteService;

    @Autowired
    private VolontarioService volontarioService;

    @Autowired
    private JWT jwt;

    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(UtenteLoginDTO body) {

        try {
            Utente foundUtente = this.utenteService.findByEmail(body.email());
            if (bcrypt.matches(body.password(), foundUtente.getPassword())) {
                return jwt.createToken(foundUtente);
            }
        } catch (Exception e) {

            try {
                Volontario foundVolontario = this.volontarioService.findByEmail(body.email());
                if (bcrypt.matches(body.password(), foundVolontario.getPassword())) {
                    return jwt.createToken(foundVolontario);
                }
            } catch (NotFoundException ex) {

                throw new UnauthorizedException("Credenziali errate!");
            }
        }


        throw new UnauthorizedException("Credenziali errate!");
    }
}
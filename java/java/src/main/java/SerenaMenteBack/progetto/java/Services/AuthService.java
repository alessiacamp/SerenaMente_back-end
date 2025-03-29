package SerenaMenteBack.progetto.java.Services;

import SerenaMenteBack.progetto.java.Entities.Utente;
import SerenaMenteBack.progetto.java.Entities.Volontario;
import SerenaMenteBack.progetto.java.Security.JWT;
import SerenaMenteBack.progetto.java.exceptions.NotFoundException;
import SerenaMenteBack.progetto.java.exceptions.UnauthorizedException;
import SerenaMenteBack.progetto.java.payloads.NewUtenteDTO;
import SerenaMenteBack.progetto.java.payloads.NewVolontarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public String updateUser(String email, NewUtenteDTO updateDTO) {

        Optional<Volontario> volontario = volontarioService.findByEmailSafe(email);
        if (volontario.isPresent()) {
            return updateVolontario(volontario.get(), updateDTO);
        }


        Optional<Utente> utente = utenteService.findByEmailSafe(email);
        if (utente.isPresent()) {
            return updateUtente(utente.get(), updateDTO);
        }

        throw new UnauthorizedException("Utente non trovato");
    }

    @Transactional(readOnly = true)
    public NewUtenteDTO getUserbyEmail(String email) {

        Optional<Volontario> volontario = volontarioService.findByEmailSafe(email);
        if (volontario.isPresent()) {
            Volontario v = volontario.get();
            return new NewUtenteDTO(
                    v.getNome(),
                    v.getCognome(),
                    v.getEmail(),
                    v.getPassword(),
                    v.getMessaggio(),
                    v.getFoto(),
                    v.getRuolo().name()
            );
        }


        Optional<Utente> utente = utenteService.findByEmailSafe(email);
        if (utente.isPresent()) {
            Utente u = utente.get();
            return new NewUtenteDTO(
                    u.getNome(),
                    u.getCognome(),
                    u.getEmail(),
                    u.getPassword(),
                    "",
                    u.getFoto(),
                    u.getRuolo().name()
            );
        }

        throw new UnauthorizedException("Utente non trovato");
    }

    private String updateUtente(Utente utente, NewUtenteDTO updateDTO) {
        if (updateDTO.nome() != null) utente.setNome(updateDTO.nome());
        if (updateDTO.cognome() != null) utente.setCognome(updateDTO.cognome());
        if (updateDTO.email() != null) utente.setEmail(updateDTO.email());
        if (updateDTO.password() != null) utente.setPassword(bcrypt.encode(updateDTO.password()));
        utente.setFoto(updateDTO.foto());

        try {
            return jwt.createToken(utenteService.findByIdAndUpdate(utente.getId(), updateDTO));
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String updateVolontario(Volontario volontario, NewUtenteDTO updateDTO) {
        String messaggio = updateDTO.messaggio().isEmpty() ? volontario.getMessaggio() : updateDTO.messaggio();
        NewVolontarioDTO updateVolontario = new NewVolontarioDTO(
                updateDTO.nome(),
                updateDTO.cognome(),
                updateDTO.email(),
                "",
                messaggio,
                updateDTO.foto()
        );

        return jwt.createToken(volontarioService.findByIdAndUpdate(volontario.getId(), updateVolontario));
    }

}
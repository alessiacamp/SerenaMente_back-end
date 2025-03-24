package SerenaMente.progetto.java.controllers;

import SerenaMente.progetto.java.Entities.Utente;
import SerenaMente.progetto.java.Services.AuthService;
import SerenaMente.progetto.java.Services.UtenteLoginDTO;
import SerenaMente.progetto.java.Services.UtenteService;
import SerenaMente.progetto.java.exceptions.BadRequestException;
import SerenaMente.progetto.java.payloads.NewUtenteDTO;
import SerenaMente.progetto.java.payloads.UtenteLoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UtenteService utenteService;

    @PostMapping("/login")
    public UtenteLoginResponseDTO login(@RequestBody UtenteLoginDTO body) {
        return new UtenteLoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente save(@RequestBody @Validated NewUtenteDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }





        return this.utenteService.save(body);
    }
}

package SerenaMenteBack.progetto.java.controllers;


import SerenaMenteBack.progetto.java.Entities.Utente;
import SerenaMenteBack.progetto.java.Services.AuthService;
import SerenaMenteBack.progetto.java.Services.UtenteLoginDTO;
import SerenaMenteBack.progetto.java.Services.UtenteService;
import SerenaMenteBack.progetto.java.exceptions.BadRequestException;
import SerenaMenteBack.progetto.java.payloads.NewUtenteDTO;
import SerenaMenteBack.progetto.java.payloads.UtenteLoginResponseDTO;
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

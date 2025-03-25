package SerenaMenteBack.progetto.java.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record EmailDTO (
    @NotEmpty(message = "L'email è un campo obbligatorio!")
    @Email(message = "L'email inserita non è un'email valida")
    String email
    )
{}

package SerenaMente.progetto.java.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewVolontarioDTO(
        @NotEmpty(message = "Il nome è obbligatorio!")
        @Size(min = 2, max = 40, message = "Il nome deve essere compreso tra 2 e 40 caratteri!")
        String nome,

        @NotEmpty(message = "Il cognome è obbligatorio!")
        @Size(min = 2, max = 40, message = "Il cognome deve essere compreso tra 2 e 40 caratteri!")
        String cognome,


        @NotEmpty(message = "L'email è un campo obbligatorio!")
        @Email(message = "L'email inserita non è un'email valida")
        String email,

        @NotEmpty(message = "La password è un campo obbligatorio!")
        @Size(message = "La password deve essere almeno di 8 caratteri")
                String password

) {
}

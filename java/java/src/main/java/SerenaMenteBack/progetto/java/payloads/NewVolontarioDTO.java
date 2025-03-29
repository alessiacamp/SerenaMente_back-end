package SerenaMenteBack.progetto.java.payloads;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewVolontarioDTO(
        @NotEmpty(message = "Il nome è obbligatorio!")
        @Size(min = 2, max = 40, message = "Il nome deve essere compreso tra 2 e 40 caratteri!")
        String nome,

        @NotEmpty(message = "Il cognome è obbligatorio!")
        @Size(min = 2, max = 40, message = "Il cognome deve essere compreso tra 2 e 40 caratteri!")
        String cognome,


        String email,


        String password,

        @NotEmpty(message = "Il messaggio è un campo obbligatorio!")
        @Size(message = "Il messaggio deve essere almeno di 8 caratteri")
        String messaggio,

        byte[] foto


) {
}

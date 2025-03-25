package SerenaMenteBack.progetto.java.Entities;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "volontari")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Volontario {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @Lob
    @Basic(fetch = FetchType.LAZY) // Aggiungi questa annotazione
    private byte[] foto;

    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String messaggio;
    private String avatarUrl;
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public UUID getId() {
        return id;
    }



    public Volontario(String nome, String cognome, String email, String password, String messaggio, byte[]foto) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.messaggio = messaggio;
        this.ruolo = Ruolo.VOLONTARIO;
        this.foto = foto;
    }



    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.ruolo.name()));
    }
}

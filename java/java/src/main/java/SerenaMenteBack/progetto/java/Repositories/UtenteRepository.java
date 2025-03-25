package SerenaMenteBack.progetto.java.Repositories;



import SerenaMenteBack.progetto.java.Entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, UUID> {
    @Transactional(readOnly = true)
    Optional<Utente> findByEmail(String email);


}

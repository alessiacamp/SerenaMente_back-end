package SerenaMenteBack.progetto.java.Repositories;


import SerenaMenteBack.progetto.java.Entities.Volontario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VolontarioRepository extends JpaRepository<Volontario, UUID> {
    @Transactional(readOnly = true)
    Optional<Volontario> findByEmail(String email);


}

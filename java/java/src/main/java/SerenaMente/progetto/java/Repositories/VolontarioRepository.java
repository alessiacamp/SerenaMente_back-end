package SerenaMente.progetto.java.Repositories;

import SerenaMente.progetto.java.Entities.Volontario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VolontarioRepository extends JpaRepository<Volontario, UUID> {
    Optional<Volontario> findByEmail(String email);


}

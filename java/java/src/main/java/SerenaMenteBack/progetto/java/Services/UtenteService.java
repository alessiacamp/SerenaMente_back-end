package SerenaMenteBack.progetto.java.Services;


import SerenaMenteBack.progetto.java.Entities.Utente;
import SerenaMenteBack.progetto.java.Repositories.UtenteRepository;
import SerenaMenteBack.progetto.java.exceptions.BadRequestException;
import SerenaMenteBack.progetto.java.exceptions.NotFoundException;
import SerenaMenteBack.progetto.java.payloads.NewUtenteDTO;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private Cloudinary cloudinaryUploader;

    @Autowired
    private PasswordEncoder bcrypt;

    public Utente save(NewUtenteDTO body) {

        this.utenteRepository.findByEmail(body.email()).ifPresent(

                utente -> {
                    throw new BadRequestException("Email " + body.email() + " già in uso!");
                }
        );


        Utente newUtente = new Utente(body.nome(), body.cognome(), body.email(), bcrypt.encode(body.password()), body.foto()
        );


        return this.utenteRepository.save(newUtente);
    }

    public Utente update(NewUtenteDTO body) {


        Utente newUtente = new Utente(body.nome(), body.cognome(), body.email(), bcrypt.encode(body.password()), body.foto()
        );


        return this.utenteRepository.save(newUtente);
    }

    public Page<Utente> findAll(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return this.utenteRepository.findAll(pageable);
    }

    public Utente findById(UUID utenteId) throws ChangeSetPersister.NotFoundException {
        return this.utenteRepository.findById(utenteId).orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }

    public Utente findByIdAndUpdate(UUID utenteId, NewUtenteDTO body) throws ChangeSetPersister.NotFoundException {

        Utente found = this.findById(utenteId);


        found.setNome(body.nome());
        found.setCognome(body.cognome());
        found.setFoto(body.foto());


        return this.utenteRepository.save(found);
    }

    public void findByIdAndDelete(UUID userId) throws ChangeSetPersister.NotFoundException {
        Utente found = this.findById(userId);
        this.utenteRepository.delete(found);
    }

    public Utente uploadAvatar(MultipartFile file, UUID utenteId) throws ChangeSetPersister.NotFoundException {

        String url = null;
        try {
            url = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        } catch (IOException e) {
            throw new BadRequestException("Ci sono stati problemi con l'upload del file!");
        }

        Utente found = this.findById(utenteId);
        found.setAvatarUrl(url);
        return this.utenteRepository.save(found);


    }

    @Transactional(readOnly = true)
    public Utente findByEmail(String email) {
        return this.utenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con email " + email + " non è stato trovato"));
    }

    @Transactional(readOnly = true)
    public Optional<Utente> findByEmailSafe(String email) {
        try {
            return Optional.of(this.findByEmail(email));
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }
}


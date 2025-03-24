package SerenaMente.progetto.java.Services;


import SerenaMente.progetto.java.Entities.Volontario;
import SerenaMente.progetto.java.Repositories.VolontarioRepository;
import SerenaMente.progetto.java.exceptions.BadRequestException;
import SerenaMente.progetto.java.exceptions.NotFoundException;
import SerenaMente.progetto.java.payloads.NewVolontarioDTO;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@Service
public class VolontarioService {
    @Autowired
    private VolontarioRepository volontarioRepository;

    @Autowired
    private Cloudinary cloudinaryUploader;

    @Autowired
    private PasswordEncoder bcrypt;

    public Volontario save(NewVolontarioDTO body) {

        this.volontarioRepository.findByEmail(body.email()).ifPresent(

                volontario -> {
                    throw new BadRequestException("Email " + body.email() + " già in uso!");
                }
        );




        Volontario newVolontario = new Volontario(body.nome(), body.cognome(), body.email(), bcrypt.encode(body.password()));


        return this.volontarioRepository.save(newVolontario);
    }

    public Page<Volontario> findAll(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return this.volontarioRepository.findAll(pageable);
    }

    public Volontario findById(UUID volontarioId) {
        return this.volontarioRepository.findById(volontarioId).orElseThrow(() -> new NotFoundException(volontarioId));
    }

    public Volontario findByIdAndUpdate(UUID volontarioId, NewVolontarioDTO body) {

        Volontario found = this.findById(volontarioId);


        if (!found.getEmail().equals(body.email())) {
            this.volontarioRepository.findByEmail(body.email()).ifPresent(

                    volontario -> {
                        throw new BadRequestException("Email " + body.email() + " già in uso!");
                    }
            );
        }





        found.setNome(body.nome());
        found.setCognome(body.cognome());
        found.setEmail(body.email());
        found.setPassword(body.password());


        return this.volontarioRepository.save(found);
    }

    public void findByIdAndDelete(UUID userId) {
        Volontario found = this.findById(userId);
        this.volontarioRepository.delete(found);
    }

    public Volontario uploadAvatar(MultipartFile file, UUID volontarioId) {

        String url = null;
        try {
            url = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        } catch (IOException e) {
            throw new BadRequestException("Ci sono stati problemi con l'upload del file!");
        }

        Volontario found = this.findById(volontarioId);
        found.setAvatarUrl(url);
        return this.volontarioRepository.save(found);



    }

    public Volontario findByEmail(String email) {
        return this.volontarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Volontario con email " + email + " non trovato!"));
    }
}


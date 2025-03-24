package SerenaMente.progetto.java.controllers;


import SerenaMente.progetto.java.Entities.Utente;
import SerenaMente.progetto.java.Services.UtenteService;
import SerenaMente.progetto.java.exceptions.BadRequestException;

import SerenaMente.progetto.java.payloads.NewUtenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/utente")
public class UtenteController {
    @Autowired
    private UtenteService utenteService;
    private UUID utenteId;


    @GetMapping
    public Page<Utente> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id") String sortBy) {

        return this.utenteService.findAll(page, size, sortBy);
    }




    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Utente save(@RequestBody @Validated NewUtenteDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.utenteService.save(body);
    }


    @GetMapping("/{utenteId}")
    public Utente findById(@PathVariable UUID utenteId) throws ChangeSetPersister.NotFoundException {
        return this.utenteService.findById(utenteId);
    }


    @PutMapping("/{utenteId}")
    public Utente findByIdAndUpdate(@PathVariable UUID utenteId, @RequestBody @Validated NewUtenteDTO body, BindingResult validationResult) throws ChangeSetPersister.NotFoundException {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }

        return this.utenteService.findByIdAndUpdate(utenteId, body);
    }


    @DeleteMapping("/{utenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID utenteId) throws ChangeSetPersister.NotFoundException {
        this.utenteService.findByIdAndDelete(utenteId);
    }

    @PatchMapping("/{utenteId}/avatar")
    public Utente uploadAvatar(@RequestParam("avatar") MultipartFile file, @PathVariable UUID utenteId) throws ChangeSetPersister.NotFoundException {

        return this.utenteService.uploadAvatar(file, utenteId);
    }
}

package SerenaMente.progetto.java.controllers;


import SerenaMente.progetto.java.Entities.Volontario;
import SerenaMente.progetto.java.Services.VolontarioService;
import SerenaMente.progetto.java.exceptions.BadRequestException;

import SerenaMente.progetto.java.payloads.NewVolontarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/volontario")
public class VolontarioController {
    @Autowired
    private VolontarioService volontarioService;


    @GetMapping
    public Page<Volontario> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "id") String sortBy) {

        return this.volontarioService.findAll(page, size, sortBy);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Volontario save(@RequestBody @Validated NewVolontarioDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.volontarioService.save(body);
    }


    @GetMapping("/{volontarioId}")
    public Volontario findById(@PathVariable UUID volontarioId) {
        return this.volontarioService.findById(volontarioId);
    }


    @PutMapping("/{volontarioId}")
    public Volontario findByIdAndUpdate(@PathVariable UUID volontarioId, @RequestBody @Validated NewVolontarioDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }

        return this.volontarioService.findByIdAndUpdate(volontarioId, body);
    }


    @DeleteMapping("/{volontarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID volontarioId) {
        this.volontarioService.findByIdAndDelete(volontarioId);
    }

    @PatchMapping("/{volontarioId}/avatar")
    public Volontario uploadAvatar(@RequestParam("avatar") MultipartFile file, @PathVariable UUID volontarioId) {

        return this.volontarioService.uploadAvatar(file, volontarioId);
    }
}
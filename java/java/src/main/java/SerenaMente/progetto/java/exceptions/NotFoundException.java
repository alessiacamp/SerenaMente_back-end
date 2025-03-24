package SerenaMente.progetto.java.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(long id) { super("Il record con id " + id + " non è stato trovato!"); }

    public NotFoundException(UUID id) { super("Il record con id " + id + " non è stato trovato!"); }

    public NotFoundException(String s) {
    }
}

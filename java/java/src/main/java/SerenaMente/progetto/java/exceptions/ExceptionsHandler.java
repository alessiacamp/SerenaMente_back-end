package SerenaMente.progetto.java.exceptions;

import SerenaMente.progetto.java.payloads.ErrorsPayload;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler {

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorsPayload handleBadrequest(BadRequestException ex) {
    return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorsPayload handleNotFound(NotFoundException ex) {
    return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorsPayload handleGeneric(Exception ex) {
    ex.printStackTrace();
    return new ErrorsPayload("Problema lato server", LocalDateTime.now());
  }

  @ExceptionHandler(UnauthorizedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorsPayload handleUnauthorized(UnauthorizedException ex) {
    return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ErrorsPayload handleForbidden(AuthorizationDeniedException ex) {
    return new ErrorsPayload("Non hai i permessi per accedere", LocalDateTime.now());
  }
}

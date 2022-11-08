package io.github.paulushcgcj.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.security.sasl.AuthenticationException;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidBearerTokenException extends AuthenticationException {
  public InvalidBearerTokenException() {
    super("Provided authentication token is invalid");
  }
}

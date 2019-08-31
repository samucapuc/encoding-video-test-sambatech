package br.com.samuel.sambatech.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import br.com.samuel.sambatech.exceptions.ErrorResponse;
import br.com.samuel.sambatech.exceptions.StandardError;
import br.com.samuel.sambatech.utils.MessageUtils;
import lombok.RequiredArgsConstructor;

@ControllerAdvice()
@RequiredArgsConstructor
public class ConstraintViolationExceptionHandler {

  private final MessageUtils messageUtils;

  @ExceptionHandler(MissingServletRequestParameterException.class)
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, WebRequest request) {
    StandardError err = new StandardError(System.currentTimeMillis(),
        HttpStatus.BAD_REQUEST.value(), messageUtils.getMessage("param.required.title"),
        messageUtils.getMessage("param.required", new Object[] {ex.getParameterName()}),
        ((ServletWebRequest) request).getRequest().getRequestURL().toString(),
        request.getDescription(false));
    return ResponseEntity.badRequest().body(err);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex,
      WebRequest request) {
    List<StandardError> listError = new ArrayList<StandardError>();
    for (String error : ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
        .collect(Collectors.toList())) {
      listError.add(new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
          messageUtils.getMessage("error.validation.title"),
          messageUtils.getMessage("error.validation", new Object[] {error}),
          messageUtils.getMessage("error.validation.details"),
          ((ServletWebRequest) request).getRequest().getRequestURL().toString()));
    }
    return new ResponseEntity<>(new ErrorResponse<>(listError), HttpStatus.BAD_REQUEST);

  }

  @ExceptionHandler(IllegalArgumentException.class)
  protected ResponseEntity<?> handleConstraintViolationException(IllegalArgumentException e,
      HttpServletRequest request) {
    StandardError err = new StandardError(System.currentTimeMillis(),
        HttpStatus.BAD_REQUEST.value(), messageUtils.getMessage("error.conversion"), e.getMessage(),
        e.getMessage(), request.getRequestURL().toString());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
  }

}

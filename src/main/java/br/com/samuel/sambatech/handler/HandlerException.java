package br.com.samuel.sambatech.handler;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.samuel.sambatech.dto.response.ResponseDTO;
import br.com.samuel.sambatech.exceptions.StandardError;
import br.com.samuel.sambatech.utils.MessageUtils;

@ControllerAdvice
public class HandlerException {
  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MessageUtils messageUtils;

  @ExceptionHandler(HttpClientErrorException.class)
  @ResponseBody
  public ResponseEntity<ResponseDTO> errorRest(HttpClientErrorException e, WebRequest request)
      throws Exception {
    ResponseDTO error = mapper.readValue(e.getResponseBodyAsString(), ResponseDTO.class);
    return ResponseEntity.status(e.getStatusCode()).body(error);
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(value = HttpStatus.FORBIDDEN)
  @ResponseBody
  public ResponseEntity<StandardError> authorization(AccessDeniedException e,
      HttpServletRequest request) {
    StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.FORBIDDEN.value(),
        messageUtils.getMessage("authorization.exception.title"),
        messageUtils.getMessage("authorization.exception"),
        messageUtils.getMessage("authorization.exception.details"),
        (request.getRequestURL().toString()));
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
  }

  @ExceptionHandler(ConversionFailedException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<StandardError> conversionError(ConversionFailedException e,
      WebRequest request) {
    String message =
        e.getCause() != null ? e.getCause().getLocalizedMessage() : e.getLocalizedMessage();
    StandardError err = new StandardError(System.currentTimeMillis(),
        HttpStatus.BAD_REQUEST.value(), messageUtils.getMessage("error.conversion"), message,
        e.getMessage(), ((ServletWebRequest) request).getRequest().getRequestURL().toString());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
  }
}

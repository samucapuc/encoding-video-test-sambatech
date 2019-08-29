package br.com.samuel.sambatech.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.samuel.sambatech.dto.response.ResponseDTO;

@ControllerAdvice
public class HandlerException {
  @Autowired
  private ObjectMapper mapper;

  @ExceptionHandler(HttpClientErrorException.class)
  @ResponseBody
  public ResponseEntity<ResponseDTO> errorRest(HttpClientErrorException e, WebRequest request)
      throws Exception {
    ResponseDTO error = mapper.readValue(e.getResponseBodyAsString(), ResponseDTO.class);
    return ResponseEntity.status(e.getStatusCode()).body(error);
  }


}

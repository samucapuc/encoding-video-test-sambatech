package br.com.samuel.sambatech.exceptions;

import org.springframework.http.HttpMethod;
import br.com.samuel.sambatech.dto.error.ErrorResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InvalidResourceException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private HttpMethod httpMethod;
  private ErrorResponseDTO error;

  public InvalidResourceException(String msg) {
    super(msg);
  }

  public InvalidResourceException(String msg, HttpMethod httpMethod, ErrorResponseDTO error) {
    super(msg);
    setHttpMethod(httpMethod);
    setError(error);
  }

  public InvalidResourceException(String msg, Throwable cause) {
    super(msg, cause);
  }

}

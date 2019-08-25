package br.com.samuel.sambatech.dto.error;

import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO implements Ivideo {
  /**
  * 
  */
  private static final long serialVersionUID = 1L;
  private Integer code;
  private String message;
  private String developerMessage;

}

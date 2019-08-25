package br.com.samuel.sambatech.dto.response;

import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO implements Ivideo {
  /**
  * 
  */
  private static final long serialVersionUID = 1L;
  private String requestId;
  private String status;
  private Object data;

}

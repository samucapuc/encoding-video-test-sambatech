package br.com.samuel.sambatech.dto.inputs;

import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InputFileHttpDTO implements Ivideo {
  /**
  * 
  */
  private static final long serialVersionUID = 1L;
  private String id;
  private String name = "Encoding UI HTTP Input";
  private String host;

  public InputFileHttpDTO(String name, String host) {
    this.name = name;
    this.host = host;
  }

}

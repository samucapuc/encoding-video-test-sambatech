package br.com.samuel.sambatech.dto.encoding;

import br.com.samuel.sambatech.enums.StatusEnum;
import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncodingDTO implements Ivideo {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String id;
  private String name;
  private StatusEnum status;

  public EncodingDTO(String id, String name) {
    this.id = id;
    this.name = name;
  }
}

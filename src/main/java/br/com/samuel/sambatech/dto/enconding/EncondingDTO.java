package br.com.samuel.sambatech.dto.enconding;

import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncondingDTO implements Ivideo {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String id;
  private String name;
}

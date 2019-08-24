package br.com.samuel.sambatech.dto.streams;

import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputFileDTO implements Ivideo {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String inputId;
  private String inputPath;

}

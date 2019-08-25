package br.com.samuel.sambatech.dto.outputs;

import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputDTO implements Ivideo {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String outputId;
  private String outputPath;

}

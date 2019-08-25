package br.com.samuel.sambatech.dto.muxings;

import java.util.List;
import br.com.samuel.sambatech.dto.outputs.OutputDTO;
import br.com.samuel.sambatech.dto.streams.StreamsInputDTO;
import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fmp4DTO implements Ivideo {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String id;
  private List<StreamsInputDTO> streams;
  private List<OutputDTO> outputs;
  private Integer segmentLength = 4;

  public Fmp4DTO(List<StreamsInputDTO> streams, List<OutputDTO> outputs) {
    this.streams = streams;
    this.outputs = outputs;
  }

}

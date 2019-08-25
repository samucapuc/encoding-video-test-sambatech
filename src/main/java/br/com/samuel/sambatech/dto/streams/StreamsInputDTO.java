package br.com.samuel.sambatech.dto.streams;

import java.util.List;
import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamsInputDTO implements Ivideo {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String id;
  private String streamId;
  private String codecConfigId;
  private List<InputFileDTO> inputStreams;

  public StreamsInputDTO(String codecConfigId, List<InputFileDTO> inputStreams) {
    this.codecConfigId = codecConfigId;
    this.inputStreams = inputStreams;
  }
}

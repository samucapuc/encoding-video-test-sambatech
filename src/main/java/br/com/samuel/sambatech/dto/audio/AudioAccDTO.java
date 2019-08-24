package br.com.samuel.sambatech.dto.audio;

import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioAccDTO implements Ivideo {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String id;
  private String name = "Audio Codec Config";
  private Integer bitrate = 128000;

}

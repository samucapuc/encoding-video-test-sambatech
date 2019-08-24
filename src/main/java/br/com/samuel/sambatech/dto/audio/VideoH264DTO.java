package br.com.samuel.sambatech.dto.audio;

import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoH264DTO implements Ivideo {
  /**
  * 
  */
  private static final long serialVersionUID = 1L;

  private String id;
  private String name = "Video Codec Config";
  private Integer bitrate = 5800000;
  private Integer width = 1920;
  private String profile = "HIGH";
}

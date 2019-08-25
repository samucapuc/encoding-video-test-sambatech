package br.com.samuel.sambatech.dto;

import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDTO implements Ivideo {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String id;
  private String outMessage;
  private String urlInputVideo;
  private String urlOutputVideo;
  private String name;
  private String status;
  private String server;

  public VideoDTO(String urlInputVideo, String name, String server) {
    this.name = name;
    this.urlInputVideo = urlInputVideo;
    this.server = server;
  }
}

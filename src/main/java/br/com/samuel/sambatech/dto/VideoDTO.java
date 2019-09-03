package br.com.samuel.sambatech.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class VideoDTO implements Ivideo {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String id;
  private String outMessage;
  private String urlInputVideo;
  private String name;
  private String server;

  public VideoDTO(String urlInputVideo, String name, String server) {
    this.name = name;
    this.urlInputVideo = urlInputVideo;
    this.server = server;
  }
}

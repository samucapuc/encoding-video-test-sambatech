package br.com.samuel.sambatech.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "video_encoding")
public class VideoEnconding implements Ivideo {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @Id
  private String id;
  private String name;
  private String status;
  private String urlInputVideo;

}

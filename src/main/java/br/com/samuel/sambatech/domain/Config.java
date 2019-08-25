package br.com.samuel.sambatech.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "video_encoding")
public class Config {
  @Id
  private String id;
  private String idOutputS3;
  private String idAudioAcc;
  private String idVideoH264;
}

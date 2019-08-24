package br.com.samuel.sambatech.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "video_encoding")
public class Config {

  private String idOutputS3;
}

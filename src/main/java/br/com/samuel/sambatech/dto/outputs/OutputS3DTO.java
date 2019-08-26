package br.com.samuel.sambatech.dto.outputs;

import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputS3DTO implements Ivideo {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String id;
  private String name = "S3 storage config";
  private String bucketName;
  private String accessKey;
  private String secretKey;

  public OutputS3DTO(String id) {
    this.id = id;
  }

  public OutputS3DTO(String bucketName, String accessKey, String secretKey) {
    this.bucketName = bucketName;
    this.accessKey = accessKey;
    this.secretKey = secretKey;
  }



}

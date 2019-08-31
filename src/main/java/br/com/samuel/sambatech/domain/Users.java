package br.com.samuel.sambatech.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class Users implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @Id
  private String id;
  @Indexed(unique = true)
  private String email;
  private String password;
}

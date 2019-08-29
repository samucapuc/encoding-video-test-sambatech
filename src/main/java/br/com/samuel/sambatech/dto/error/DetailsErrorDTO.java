package br.com.samuel.sambatech.dto.error;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailsErrorDTO implements Serializable {
  /**
  * 
  */
  private static final long serialVersionUID = 1L;

  private String id;
  private String date;
  private String type;
  private String text;
  private String field;
}

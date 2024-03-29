package br.com.samuel.sambatech.dto.error;

import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO implements Serializable {
  /**
  * 
  */
  private static final long serialVersionUID = 1L;
  private Integer code;
  private String message;
  private String developerMessage;
  private Set<LinkErrorDTO> links;
  private Set<DetailsErrorDTO> details;

}

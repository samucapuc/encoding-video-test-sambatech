package br.com.samuel.sambatech.exceptions;

import java.io.Serializable;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import br.com.samuel.sambatech.dto.error.DetailsErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class StandardError implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long timestamp;
  private Integer status;
  private String error;
  private String message;
  private String details;
  private String path;
  private Set<DetailsErrorDTO> completionDetails;

}

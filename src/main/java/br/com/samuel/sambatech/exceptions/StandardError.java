package br.com.samuel.sambatech.exceptions;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StandardError implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long timestamp;
  private Integer status;
  private String error;
  private String message;
  @JsonInclude(Include.NON_NULL)
  private String details;
  private String path;
}

package br.com.samuel.sambatech.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FileException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public FileException(String msg) {
    super(msg);
  }

  public FileException(String msg, Throwable cause) {
    super(msg, cause);
  }

}

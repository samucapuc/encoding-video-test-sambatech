package br.com.samuel.sambatech.exceptions;

import org.springframework.http.HttpMethod;


public class InvalidResourceException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private HttpMethod httpMethod;

  public InvalidResourceException(String msg) {
    super(msg);
  }

  public InvalidResourceException(String msg, HttpMethod httpMethod) {
    super(msg);
    this.setHttpMethod(httpMethod);
  }

  public InvalidResourceException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(HttpMethod httpMethod) {
    this.httpMethod = httpMethod;
  }

}

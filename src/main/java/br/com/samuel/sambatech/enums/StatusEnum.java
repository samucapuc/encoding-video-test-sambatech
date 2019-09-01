package br.com.samuel.sambatech.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusEnum {
  CREATED, STARTED, QUEUED, DOWNLOADED, ANALYZED, ENCODING, FINISHED, ERROR, RUNNING;
}

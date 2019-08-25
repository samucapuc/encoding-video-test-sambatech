package br.com.samuel.sambatech.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import br.com.samuel.sambatech.interfaces.Ivideo;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ConverterUtils {

  private final ModelMapper mapper;

  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T> Ivideo convertObject(T origin, Class classOut) {
    return (Ivideo) mapper.map(origin, classOut);
  }
}

package br.com.samuel.sambatech.utils;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class MessageUtils {

  @Autowired
  private MessageSource message;

  public String getMessage(String key) {
    // Locale ptBr = new Locale("pt", "BR");
    return message.getMessage(key, null, Locale.getDefault());
  }

  public String getMessage(String key, Object... args) {
    return message.getMessage(key, args, Locale.getDefault());
  }
}

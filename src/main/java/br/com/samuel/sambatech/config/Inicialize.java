package br.com.samuel.sambatech.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import br.com.samuel.sambatech.domain.Users;
import br.com.samuel.sambatech.services.UsersService;
import br.com.samuel.sambatech.services.VideoEncodingService;

@Configuration
public class Inicialize implements CommandLineRunner {

  private final Logger LOG = LoggerFactory.getLogger(Inicialize.class);

  @Autowired
  private VideoEncodingService videoEncondingService;

  @Autowired
  private UsersService usersService;

  @Override
  public void run(String... args) throws Exception {
    videoEncondingService.createEncodingsPreConfig();
    try {
      usersService.save(new HashSet<Users>(
          Arrays.asList(new Users(UUID.randomUUID().toString(), "cliente@sambatech.com.br",
              "$2a$10$LnkfERzOsgQ8uaYrDKb0AuqQLR9PpOsTWH9TY32vCmKl5LlL/Ejsu"))));
    } catch (DuplicateKeyException e) {
      LOG.info("Mensagem tratada de usuário duplicado ao iniciar");
    }

  }
}

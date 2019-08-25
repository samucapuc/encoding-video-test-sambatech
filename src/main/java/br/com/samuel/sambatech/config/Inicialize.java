package br.com.samuel.sambatech.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import br.com.samuel.sambatech.services.VideoEncodingService;

@Configuration
public class Inicialize implements CommandLineRunner {

  @Autowired
  private VideoEncodingService videoEncondingService;

  @Override
  public void run(String... args) throws Exception {
    videoEncondingService.createEncodingsPreConfig();
  }
}

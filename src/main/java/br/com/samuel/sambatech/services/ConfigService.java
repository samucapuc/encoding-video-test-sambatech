package br.com.samuel.sambatech.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.samuel.sambatech.domain.Config;
import br.com.samuel.sambatech.repositories.ConfigRepository;

@Service
public class ConfigService {

  @Autowired
  private ConfigRepository configRepository;

  public Config findFirst() {
    return configRepository.findAll().stream().findFirst().orElse(null);
  }

  public void save(Config cf) {
    configRepository.save(cf);
  }
}

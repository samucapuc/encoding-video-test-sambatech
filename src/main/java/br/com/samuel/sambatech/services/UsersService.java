package br.com.samuel.sambatech.services;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.samuel.sambatech.domain.Users;
import br.com.samuel.sambatech.repositories.UsersRepository;
import io.jsonwebtoken.lang.Collections;

@Service
public class UsersService {

  @Autowired
  private UsersRepository repo;

  public void save(Set<Users> users) {
    if (!Collections.isEmpty(users)) {
      users.forEach(u -> repo.save(u));
    }
  }
}

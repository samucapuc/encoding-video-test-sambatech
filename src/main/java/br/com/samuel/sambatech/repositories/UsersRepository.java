package br.com.samuel.sambatech.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import br.com.samuel.sambatech.domain.Users;

@Repository
public interface UsersRepository extends MongoRepository<Users, Long> {

  public Users findByEmail(String email);
}

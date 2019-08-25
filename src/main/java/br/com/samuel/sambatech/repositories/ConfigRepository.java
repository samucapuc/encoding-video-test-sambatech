package br.com.samuel.sambatech.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import br.com.samuel.sambatech.domain.Config;

@Repository
public interface ConfigRepository extends MongoRepository<Config, String> {

}

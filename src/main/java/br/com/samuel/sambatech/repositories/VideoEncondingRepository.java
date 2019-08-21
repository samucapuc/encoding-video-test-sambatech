package br.com.samuel.sambatech.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import br.com.samuel.sambatech.domain.VideoEnconding;

@Repository
public interface VideoEncondingRepository extends MongoRepository<VideoEnconding, String> {

}

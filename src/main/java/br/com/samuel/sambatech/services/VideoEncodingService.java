package br.com.samuel.sambatech.services;

import org.springframework.stereotype.Service;
import br.com.samuel.sambatech.repositories.VideoEncondingRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VideoEncodingService {

  private final VideoEncondingRepository videoEncondingRepository;

}

package br.com.samuel.sambatech.controllers;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.samuel.sambatech.dto.VideoDTO;
import br.com.samuel.sambatech.services.VideoEncodingService;

@RestController
@RequestMapping(value = "/api/encoding")
public class VideoEncondingController {

  @Autowired
  private VideoEncodingService service;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> encoding(@RequestParam("file") MultipartFile file) throws Exception {
    VideoDTO v = service.encondingVideo(file);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(v.getId()).toUri();
    return ResponseEntity.created(uri).build();
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<String> getEncoding(@PathVariable() String id) {
    VideoDTO v = service.getEncodingDetails(id);
    return ResponseEntity.ok().body(v.getOutMessage());
  }
}

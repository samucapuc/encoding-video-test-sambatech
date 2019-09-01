package br.com.samuel.sambatech.controllers.encodings;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.samuel.sambatech.annotations.ValidVideo;
import br.com.samuel.sambatech.dto.VideoDTO;
import br.com.samuel.sambatech.services.VideoEncodingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/encoding")
@Validated
@Api(value = "/api/encoding",
    description = "Serviços para gerar encoding e consultar situação da codificação")
public class VideoEncondingController {

  @Autowired
  private VideoEncodingService service;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ApiOperation(
      value = "Faça o upload de um vídeo no formato (https://bitmovin.com/docs/encoding/articles/supported-input-and-output-formats) para codificar")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Erro de validação"),
      @ApiResponse(code = 200, message = "Sucesso"),
      @ApiResponse(code = 400, message = "Erro de conversão"),
      @ApiResponse(code = 401, message = "Usuário não autenticado"),
      @ApiResponse(code = 403, message = "Usuário não autorizado")})
  public ResponseEntity<Void> encoding(@ValidVideo @RequestParam("file") MultipartFile file)
      throws Exception {
    VideoDTO v = service.encondingVideo(file);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(v.getId()).toUri();
    return ResponseEntity.created(uri).build();
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "Informe o id para consultar a situação da codificação")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Erro de validação"),
      @ApiResponse(code = 200, message = "Sucesso"),
      @ApiResponse(code = 400, message = "Erro de conversão"),
      @ApiResponse(code = 401, message = "Usuário não autenticado"),
      @ApiResponse(code = 403, message = "Usuário não autorizado")})
  public ResponseEntity<VideoDTO> getEncoding(@PathVariable() String id) {
    VideoDTO v = service.getEncodingDetails(id);
    return ResponseEntity.ok().body(v);
  }
}

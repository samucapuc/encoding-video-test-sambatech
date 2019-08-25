package br.com.samuel.sambatech.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import br.com.samuel.sambatech.domain.Config;
import br.com.samuel.sambatech.dto.audio.AudioAccDTO;
import br.com.samuel.sambatech.dto.audio.VideoH264DTO;
import br.com.samuel.sambatech.dto.outputs.OutputS3DTO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VideoEncodingService extends MainService {

  public static final String KEY_STREAM_AUDIO = "KEY_STREAM_AUDIO";
  public static final String KEY_STREAM_VIDEO = "KEY_STREAM_AUDIO";

  private final ConfigService configService;

  @Value("${bucket.videos.aws.s3}")
  private String bucketS3;

  @Value("${accessKey.acess.bucket}")
  private String accessKeyS3;

  @Value("${secretKey.acess.bucket}")
  private String secretKeyS3;

  @Value("${bit.movin.url.api.output.s3}")
  private String urlEndpointOutputS3;

  @Value("${bit.movin.url.api.configurations.audio.aac}")
  private String urlEndpointConfigAudioAcc;

  @Value("${bit.movin.url.api.configurations.video.h264}")
  private String urlEndpointConfigVideoH264;



  /**
   * Gera as configurações e grava os ids no banco para nao precisar de gerar a cada requisição de
   * encoding. Em uma aplicação onde se escolhe as resoluções e áudios, os audios e vídeos podem ser
   * gerados a cada requisição
   */
  public void createEncodingsPreConfig() {
    Config cf = configService.findFirst();
    if (cf == null) {
      cf = new Config();
      createOutputS3(cf);
      createAudioAcc(cf);
      createVideoH264(cf);
      configService.save(cf);
    }
  }

  /**
   * Cria o output na aws s3 para gravar os videos encoded
   * 
   * @param cf
   */
  public void createOutputS3(Config cf) {
    // encoding/outputs/s3
    OutputS3DTO os3 = returnObject(urlEndpointOutputS3,
        new OutputS3DTO(bucketS3, accessKeyS3, secretKeyS3), new TypeReference<OutputS3DTO>() {});
    System.out.println("IdOutputS3=" + os3.getId());
    cf.setIdOutputS3(os3.getId());
  }

  /**
   * Cria a configuração de audio acc
   * 
   * @param cf
   */
  public void createAudioAcc(Config cf) {
    // encoding/configurations/audio/aac
    AudioAccDTO aacc = returnObject(urlEndpointConfigAudioAcc, new AudioAccDTO(),
        new TypeReference<AudioAccDTO>() {});
    System.out.println("AudioAccId=" + aacc.getId());
    cf.setIdAudioAcc(aacc.getId());
  }

  /**
   * Cria a configuração de video
   * 
   * @param cf
   */
  public void createVideoH264(Config cf) {
    // encoding/configurations/video/h264
    VideoH264DTO videoH264 = returnObject(urlEndpointConfigVideoH264, new VideoH264DTO(),
        new TypeReference<VideoH264DTO>() {});
    System.out.println("VideoH264Id=" + videoH264.getId());
    cf.setIdVideoH264(videoH264.getId());
  }

}

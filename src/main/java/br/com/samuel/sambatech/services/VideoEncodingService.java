package br.com.samuel.sambatech.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import br.com.samuel.sambatech.domain.Config;
import br.com.samuel.sambatech.domain.VideoEnconding;
import br.com.samuel.sambatech.dto.VideoDTO;
import br.com.samuel.sambatech.dto.audio.AudioAccDTO;
import br.com.samuel.sambatech.dto.audio.VideoH264DTO;
import br.com.samuel.sambatech.dto.encoding.EncodingDTO;
import br.com.samuel.sambatech.dto.inputs.InputFileHttpDTO;
import br.com.samuel.sambatech.dto.muxings.Fmp4DTO;
import br.com.samuel.sambatech.dto.outputs.OutputDTO;
import br.com.samuel.sambatech.dto.outputs.OutputS3DTO;
import br.com.samuel.sambatech.dto.streams.InputFileDTO;
import br.com.samuel.sambatech.dto.streams.StreamsInputDTO;
import br.com.samuel.sambatech.repositories.VideoEncondingRepository;
import br.com.samuel.sambatech.utils.ConverterUtils;
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

  @Value("${bit.movin.url.api.inputs.http}")
  private String urlEndpointInputHttpFile;

  @Value("${bit.movin.url.api.encoding}")
  private String urlEndpointEncoding;

  @Value("${bit.movin.url.api.streams}")
  private String urlEndpointStreams;

  @Value("${bit.movin.url.api.muxings.fmp4}")
  private String urlEndpointMuxingsFmp4;

  private final ConverterUtils converter;

  private final VideoEncondingRepository videoEncondingRepository;

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

  public VideoDTO encondingVideo(VideoDTO vdto) throws Exception {
    VideoEnconding ve = videoEncondingRepository.findByName(vdto.getName());
    if (ve != null) {
      return (VideoDTO) converter.convertObject(ve, VideoDTO.class);
    }
    ve = videoEncondingRepository
        .save((VideoEnconding) converter.convertObject(vdto, VideoEnconding.class));
    vdto.setId(ve.getId());
    // get config output s3 mongodb
    Config cf = configService.findFirst();
    System.out.println("IdOutputS3=" + cf.getIdOutputS3());
    System.out.println("AudioAccId=" + cf.getIdAudioAcc());
    System.out.println("VideoH264Id=" + cf.getIdVideoH264());

    InputFileHttpDTO ifh = createHttpInput(vdto);
    System.out.println("IdInputFile=" + ifh.getId());
    EncodingDTO encoding = createEncoding(vdto);
    System.out.println("IdEncoding=" + encoding.getId());
    Map<String, StreamsInputDTO> mapStreams = createStreamsAudioAndVideo(vdto, cf, ifh, encoding);
    createFmp4AudioAndVideo(ve, mapStreams, encoding, cf);


    return (VideoDTO) converter.convertObject(ve, VideoDTO.class);
  }

  private InputFileHttpDTO createHttpInput(VideoDTO vdto) throws Exception {
    /// encoding/inputs/http
    InputFileHttpDTO ifh = returnObject(urlEndpointInputHttpFile,
        new InputFileHttpDTO(vdto.getId(), vdto.getName(), getDomainUrl(vdto.getUrlInputVideo())),
        new TypeReference<InputFileHttpDTO>() {});
    return ifh;
  }

  private EncodingDTO createEncoding(VideoDTO vdto) {
    // encoding/encodings
    EncodingDTO encoding = returnObject(urlEndpointEncoding, new EncodingDTO(vdto.getName()),
        new TypeReference<EncodingDTO>() {});
    return encoding;
  }

  private Map<String, StreamsInputDTO> createStreamsAudioAndVideo(VideoDTO vdto, Config cf,
      InputFileHttpDTO ifh, EncodingDTO encoding) {
    Map<String, StreamsInputDTO> mapStreams = new HashMap<>();
    // encoding/encodings/{encoding_id}/streams
    StreamsInputDTO siAudio =
        returnObject(urlEndpointStreams.replaceAll("\\{encoding_id}", encoding.getId()),
            new StreamsInputDTO(cf.getIdAudioAcc(),
                Arrays.asList(new InputFileDTO(ifh.getId(), vdto.getName()))),
            new TypeReference<StreamsInputDTO>() {});
    System.out.println("IdStreamAudio=" + siAudio.getId());
    siAudio.setStreamId(siAudio.getId());
    mapStreams.put(KEY_STREAM_AUDIO, siAudio);

    StreamsInputDTO siVideo =
        returnObject(urlEndpointStreams.replaceAll("\\{encoding_id}", encoding.getId()),
            new StreamsInputDTO(cf.getIdVideoH264(),
                Arrays.asList(new InputFileDTO(ifh.getId(), vdto.getName()))),
            new TypeReference<StreamsInputDTO>() {});
    System.out.println("IdStreamVideo=" + siVideo.getId());
    siVideo.setStreamId(siVideo.getId());
    mapStreams.put(KEY_STREAM_VIDEO, siVideo);
    return mapStreams;
  }

  private void createFmp4AudioAndVideo(VideoEnconding ve, Map<String, StreamsInputDTO> mapStreams,
      EncodingDTO encoding, Config cf) {
    String pathS3 = "ENCODING/" + ve.getId() + "/AUDIO";
    Fmp4DTO fmp4Audio =
        returnObject(urlEndpointMuxingsFmp4.replaceAll("\\{encoding_id}", encoding.getId()),
            new Fmp4DTO(Arrays.asList(mapStreams.get(KEY_STREAM_AUDIO)),
                Arrays.asList(new OutputDTO(cf.getIdOutputS3(), pathS3 + "/AUDIO"))),
            new TypeReference<Fmp4DTO>() {});
    System.out.println("IdFmp4Audio=" + fmp4Audio.getId());

    Fmp4DTO fmp4Video =
        returnObject(urlEndpointMuxingsFmp4.replaceAll("\\{encoding_id}", encoding.getId()),
            new Fmp4DTO(Arrays.asList(mapStreams.get(KEY_STREAM_VIDEO)),
                Arrays.asList(new OutputDTO(cf.getIdOutputS3(), pathS3 + "/VIDEO"))),
            new TypeReference<Fmp4DTO>() {});
    System.out.println("IdFmp4Video=" + fmp4Video.getId());
  }
}

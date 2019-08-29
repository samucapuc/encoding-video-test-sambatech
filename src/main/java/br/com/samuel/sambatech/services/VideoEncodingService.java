package br.com.samuel.sambatech.services;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import br.com.samuel.sambatech.domain.Config;
import br.com.samuel.sambatech.dto.VideoDTO;
import br.com.samuel.sambatech.dto.audio.AudioAccDTO;
import br.com.samuel.sambatech.dto.audio.VideoH264DTO;
import br.com.samuel.sambatech.dto.encoding.EncodingDTO;
import br.com.samuel.sambatech.dto.inputs.InputFileHttpDTO;
import br.com.samuel.sambatech.dto.muxings.Mp4DTO;
import br.com.samuel.sambatech.dto.outputs.OutputDTO;
import br.com.samuel.sambatech.dto.outputs.OutputS3DTO;
import br.com.samuel.sambatech.dto.streams.InputFileDTO;
import br.com.samuel.sambatech.dto.streams.StreamsInputDTO;
import br.com.samuel.sambatech.utils.FileUtils;
import br.com.samuel.sambatech.utils.MessageUtils;

@Service
public class VideoEncodingService {

  private static final String KEY_STREAM_AUDIO = "KEY_STREAM_AUDIO";
  private static final String KEY_STREAM_VIDEO = "KEY_STREAM_VIDEO";
  private static final String _AUDIO = "_audio";
  private static final String _VIDEO = "_video";

  @Autowired
  private MainService mainService;

  @Autowired
  private ConfigService configService;

  @Autowired
  private S3Service s3Service;

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

  @Value("${bit.movin.url.api.muxings.mp4}")
  private String urlEndpointMuxingsmp4;

  @Value("${bit.movin.url.api.start}")
  private String urlEndpointStartEncoding;

  @Value("${path.encodings.videos.s3}")
  private String pathVideosS3;

  @Value("${link.videos.s3}")
  private String linkVideosS3;

  @Value("${bit.movin.url.api.encoding.details}")
  private String urlEndpointEncodingDetails;

  @Value("${bucket.videos.aws.s3.directory.original}")
  private String pathDirectoryOriginal;

  @Value("${format.encoding}")
  private String formatFileExtension;

  @Autowired
  private MessageUtils messageUtils;

  @Autowired
  private FileUtils fileUtils;

  /**
   * Gera as configurações e grava os ids no banco para nao precisar de gerar a cada requisição de
   * encoding. Em uma aplicação onde se escolhe as resoluções e áudios, os audios e vídeos podem ser
   * gerados a cada requisição
   */
  public void createEncodingsPreConfig() {
    Config cf = configService.findFirst();
    if (ObjectUtils.isEmpty(cf)) {
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
    cf.setIdOutputS3(mainService.returnObject(urlEndpointOutputS3,
        new OutputS3DTO(bucketS3, accessKeyS3, secretKeyS3), HttpMethod.POST).getId());
  }

  /**
   * Cria a configuração de audio acc
   * 
   * @param cf
   */
  public void createAudioAcc(Config cf) {
    // encoding/configurations/audio/aac
    cf.setIdAudioAcc(mainService
        .returnObject(urlEndpointConfigAudioAcc, new AudioAccDTO(), HttpMethod.POST).getId());
  }

  /**
   * Cria a configuração de video
   * 
   * @param cf
   */
  public void createVideoH264(Config cf) {
    // encoding/configurations/video/h264
    cf.setIdVideoH264(mainService
        .returnObject(urlEndpointConfigVideoH264, new VideoH264DTO(), HttpMethod.POST).getId());
  }

  public VideoDTO encondingVideo(MultipartFile multipartFile) throws Exception {
    URL url = s3Service.uploadFile(multipartFile);
    VideoDTO vdto =
        new VideoDTO(url.toString(), multipartFile.getOriginalFilename(), url.getAuthority());
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
    createMp4AudioAndVideo(mapStreams, encoding, cf);
    startEncoding(encoding);
    vdto.setId(encoding.getId());
    System.out.println("Id=" + vdto.getId());
    return vdto;
  }

  private void startEncoding(EncodingDTO encoding) {
    // encoding/encodings/{encoding_id}/start
    encoding = mainService.returnObject(
        urlEndpointStartEncoding.replaceAll("\\{encoding_id}", encoding.getId()), encoding,
        HttpMethod.POST);
  }

  private InputFileHttpDTO createHttpInput(VideoDTO vdto) throws Exception {
    /// encoding/inputs/http
    return mainService.returnObject(urlEndpointInputHttpFile, new InputFileHttpDTO(vdto.getId(),
        pathDirectoryOriginal + vdto.getName(), vdto.getServer()), HttpMethod.POST);
  }

  private EncodingDTO createEncoding(VideoDTO vdto) {
    // encoding/encodings
    return mainService.returnObject(urlEndpointEncoding,
        new EncodingDTO(null,
            fileUtils.changeExtensionFile(vdto.getName(), _VIDEO, formatFileExtension)),
        HttpMethod.POST);
  }

  private Map<String, StreamsInputDTO> createStreamsAudioAndVideo(VideoDTO vdto, Config cf,
      InputFileHttpDTO ifh, EncodingDTO encoding) {
    String endpoint = urlEndpointStreams.replaceAll("\\{encoding_id}", encoding.getId());
    Map<String, StreamsInputDTO> mapStreams = new HashMap<>();
    // encoding/encodings/{encoding_id}/streams
    StreamsInputDTO siAudio = mainService.returnObject(endpoint,
        new StreamsInputDTO(cf.getIdAudioAcc(),
            Arrays.asList(new InputFileDTO(ifh.getId(), pathDirectoryOriginal + vdto.getName()))),
        HttpMethod.POST);
    System.out.println("IdStreamAudio=" + siAudio.getId());
    siAudio.setStreamId(siAudio.getId());
    mapStreams.put(KEY_STREAM_AUDIO, siAudio);


    StreamsInputDTO siVideo = mainService.returnObject(endpoint,
        new StreamsInputDTO(cf.getIdVideoH264(),
            Arrays.asList(new InputFileDTO(ifh.getId(), pathDirectoryOriginal + vdto.getName()))),
        HttpMethod.POST);
    System.out.println("IdStreamVideo=" + siVideo.getId());
    siVideo.setStreamId(siVideo.getId());
    mapStreams.put(KEY_STREAM_VIDEO, siVideo);
    return mapStreams;
  }

  private void createMp4AudioAndVideo(Map<String, StreamsInputDTO> mapStreams, EncodingDTO encoding,
      Config cf) {
    String endpoint = urlEndpointMuxingsmp4.replaceAll("\\{encoding_id}", encoding.getId());
    String path = pathVideosS3.replaceAll("\\{encoding_id}", encoding.getId());
    Mp4DTO mp4Audio = mainService.returnObject(endpoint,
        new Mp4DTO(Arrays.asList(mapStreams.get(KEY_STREAM_AUDIO)),
            Arrays.asList(new OutputDTO(cf.getIdOutputS3(), path)),
            fileUtils.changeExtensionFile(encoding.getName(), _AUDIO, formatFileExtension)),
        HttpMethod.POST);
    System.out.println("Idmp4Audio=" + mp4Audio.getId());

    Mp4DTO mp4Video = mainService.returnObject(endpoint,
        new Mp4DTO(Arrays.asList(mapStreams.get(KEY_STREAM_VIDEO)),
            Arrays.asList(new OutputDTO(cf.getIdOutputS3(), path)), fileUtils
                .changeExtensionFile(encoding.getName(), StringUtils.EMPTY, formatFileExtension)),
        HttpMethod.POST);
    System.out.println("Idmp4Video=" + mp4Video.getId());
  }

  public VideoDTO getEncodingDetails(String id) {
    VideoDTO vdto = new VideoDTO();
    EncodingDTO encoding =
        mainService.returnObject(urlEndpointEncodingDetails.replaceAll("\\{encoding_id}", id),
            new EncodingDTO(id, null), HttpMethod.GET);
    String msg = StringUtils.EMPTY;
    switch (encoding.getStatus()) {
      case FINISHED:
        msg = messageUtils.getMessage("msg.encoding.sucess", new Object[] {
            linkVideosS3.replaceAll("\\{encoding_id}", id).concat(encoding.getName())});
        break;
      case ERROR:
        msg = messageUtils.getMessage("msg.encoding.error");
        break;
      default:
        msg = messageUtils.getMessage("msg.encoding.running");
        break;
    }
    vdto.setOutMessage(msg);
    return vdto;
  }
}

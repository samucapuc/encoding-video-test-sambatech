package br.com.samuel.sambatech;

import static org.junit.Assert.assertEquals;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
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
import br.com.samuel.sambatech.enums.StatusEnum;
import br.com.samuel.sambatech.services.ConfigService;
import br.com.samuel.sambatech.services.MainService;
import br.com.samuel.sambatech.services.S3Service;
import br.com.samuel.sambatech.services.VideoEncodingService;
import br.com.samuel.sambatech.utils.FileUtils;
import br.com.samuel.sambatech.utils.MessageUtils;

@RunWith(MockitoJUnitRunner.class)
public class EncodingVideoTestSambatechApplicationTests {

  @InjectMocks
  private VideoEncodingService videoEncodingService;

  @Mock
  private S3Service s3Service;

  @Mock
  private MainService mainService;

  @Mock
  private ConfigService configService;

  @Mock
  private VideoEncodingService videoEncodingServiceMock;
  @Mock
  private FileUtils fileUtils;
  @Mock
  private MessageUtils messageUtils;

  @Before
  public void setUp() {
    setPropertiesApplicationResource(new String[] {"urlEndpointStreams", "urlEndpointMuxingsmp4",
        "pathVideosS3", "urlEndpointStartEncoding", "urlEndpointEncodingDetails", "linkVideosS3"});
  }

  private void setPropertiesApplicationResource(String[] properties) {
    Arrays.asList(properties).forEach(p -> {
      ReflectionTestUtils.setField(videoEncodingService, p, StringUtils.EMPTY);
    });
  }

  @Test
  public void createOutputS3() {
    OutputS3DTO s3 = new OutputS3DTO("1");
    Config config = new Config();
    Mockito.when(mainService.returnObject(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(s3);
    videoEncodingService.createOutputS3(config);
    assertEquals(config.getIdOutputS3(), "1");
  }

  @Test
  public void createAudioAcc() {
    AudioAccDTO acc = new AudioAccDTO("2", "teste", 1234);
    Config config = new Config();
    Mockito.when(mainService.returnObject(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(acc);
    videoEncodingService.createAudioAcc(config);
    assertEquals(config.getIdAudioAcc(), "2");
  }

  @Test
  public void createVideoH264() {
    VideoH264DTO vh264 = new VideoH264DTO("3", "teste", 1234, 1920, "HIGH");
    Config config = new Config();
    Mockito.when(mainService.returnObject(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(vh264);
    videoEncodingService.createVideoH264(config);
    assertEquals(config.getIdVideoH264(), "3");
  }

  @Test
  public void encondingVideo() throws Exception {
    URL url = new URL("http", "sambatech-videos-teste.s3.amazonaws.com", "teste.mkv");
    Config cf = new Config("1", UUID.randomUUID().toString(), UUID.randomUUID().toString(),
        UUID.randomUUID().toString());
    MockMultipartFile multipartFile =
        new MockMultipartFile("teste.mkv", "teste.mkv", StringUtils.EMPTY, new byte[] {});
    InputFileHttpDTO ifh = new InputFileHttpDTO(UUID.randomUUID().toString(), "teste.mkv",
        "sambatech-videos-teste.s3.amazonaws.com");
    EncodingDTO encoding =
        new EncodingDTO(UUID.randomUUID().toString(), "teste.mkv", StatusEnum.CREATED);
    StreamsInputDTO siAudio = new StreamsInputDTO(UUID.randomUUID().toString(),
        UUID.randomUUID().toString(), cf.getIdAudioAcc(), Arrays.asList(
            new InputFileDTO(ifh.getId(), "ORIGINAL/" + multipartFile.getOriginalFilename())));
    StreamsInputDTO siVideo = new StreamsInputDTO(UUID.randomUUID().toString(),
        UUID.randomUUID().toString(), cf.getIdVideoH264(), Arrays.asList(
            new InputFileDTO(ifh.getId(), "ORIGINAL/" + multipartFile.getOriginalFilename())));
    Mp4DTO mp4Audio = new Mp4DTO(UUID.randomUUID().toString(), Arrays.asList(siAudio),
        Arrays.asList(new OutputDTO(cf.getIdOutputS3(), "ENCODINGS/" + encoding.getId())),
        "teste_audio.mp4");
    Mp4DTO mp4Video = new Mp4DTO(UUID.randomUUID().toString(), Arrays.asList(siVideo),
        Arrays.asList(new OutputDTO(cf.getIdOutputS3(), "ENCODINGS/" + encoding.getId())),
        "teste_video.mp4");

    Mockito.when(s3Service.uploadFile(Mockito.any())).thenReturn(url);
    Mockito.when(configService.findFirst()).thenReturn(cf);
    Mockito
        .when(
            fileUtils.changeExtensionFile(Mockito.any(), Mockito.contains("_video"), Mockito.any()))
        .thenReturn("teste_video.mp4");
    Mockito
        .when(
            fileUtils.changeExtensionFile(Mockito.any(), Mockito.contains("_audio"), Mockito.any()))
        .thenReturn("teste_audio.mp4");
    Mockito.when(
        mainService.returnObject(Mockito.any(), Mockito.any(InputFileHttpDTO.class), Mockito.any()))
        .thenReturn(ifh);
    Mockito
        .when(
            mainService.returnObject(Mockito.any(), Mockito.any(EncodingDTO.class), Mockito.any()))
        .thenReturn(encoding);
    Mockito.when(
        mainService.returnObject(Mockito.any(), Mockito.any(StreamsInputDTO.class), Mockito.any()))
        .thenAnswer(inv -> {
          StreamsInputDTO s = ((StreamsInputDTO) inv.getArgument(1));
          return s.getCodecConfigId().equals(cf.getIdVideoH264()) ? siVideo : siAudio;
        });
    Mockito.when(mainService.returnObject(Mockito.any(), Mockito.any(Mp4DTO.class), Mockito.any()))
        .thenAnswer(inv -> {
          Mp4DTO m = ((Mp4DTO) inv.getArgument(1));
          return m.getStreams().stream().findFirst().get().getCodecConfigId()
              .equals(cf.getIdVideoH264()) ? mp4Video : mp4Audio;
        });
    VideoDTO vdto = videoEncodingService.encondingVideo(multipartFile);
    assertEquals(ifh.getHost(), url.getAuthority());
    assertEquals(siAudio.getCodecConfigId(), cf.getIdAudioAcc());
    assertEquals(siVideo.getCodecConfigId(), cf.getIdVideoH264());
    assertEquals(mp4Audio.getStreams().stream().findFirst().get().getCodecConfigId(),
        cf.getIdAudioAcc());
    assertEquals(mp4Video.getStreams().stream().findFirst().get().getCodecConfigId(),
        cf.getIdVideoH264());
    assertEquals(encoding.getStatus(), StatusEnum.CREATED);
    assertEquals(vdto.getId(), encoding.getId());
  }

  @Test
  public void getEncodingDetails() {
    EncodingDTO encoding =
        new EncodingDTO(UUID.randomUUID().toString(), "teste.mp4", StatusEnum.FINISHED);
    String url = "https://sambatech-videos-teste.s3.amazonaws.com/ENCODINGS/" + encoding.getId()
        + "/" + encoding.getName();
    String msg = "O vídeo foi codificado com sucesso. Acesse em: " + url;
    Mockito
        .when(
            mainService.returnObject(Mockito.any(), Mockito.any(EncodingDTO.class), Mockito.any()))
        .thenReturn(encoding);
    Mockito.when(messageUtils.getMessage(Mockito.anyString(), Mockito.any())).thenReturn(msg);
    VideoDTO vdto = videoEncodingService.getEncodingDetails(encoding.getId());
    assertEquals(vdto.getOutMessage(), msg);
  }
}

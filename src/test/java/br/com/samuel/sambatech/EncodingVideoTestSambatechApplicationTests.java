package br.com.samuel.sambatech;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import br.com.samuel.sambatech.domain.Config;
import br.com.samuel.sambatech.dto.outputs.OutputS3DTO;
import br.com.samuel.sambatech.services.MainService;
import br.com.samuel.sambatech.services.VideoEncodingService;

@RunWith(MockitoJUnitRunner.class)
public class EncodingVideoTestSambatechApplicationTests {

  @InjectMocks
  private VideoEncodingService videoEncodingService;

  @Mock
  private MainService mainService;


  @Test
  public void createOutputS3() {
    OutputS3DTO s3 = new OutputS3DTO("1");
    Config config = new Config();
    Mockito
        .when(mainService.returnObject(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(s3);
    videoEncodingService.createOutputS3(config);
    assertEquals(config.getIdOutputS3(), "1");
  }

}

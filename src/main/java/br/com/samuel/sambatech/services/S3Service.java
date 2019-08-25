package br.com.samuel.sambatech.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import br.com.samuel.sambatech.exceptions.FileException;

@Service
public class S3Service {

  private final Logger LOG = LoggerFactory.getLogger(S3Service.class);

  @Autowired
  private AmazonS3 s3client;

  @Value("${bucket.videos.aws.s3}")
  private String bucketS3;

  @Value("${bucket.videos.aws.s3.directory.original}")
  private String directory;

  public URL uploadFile(MultipartFile multipartFile) {
    try {
      return uploadFile(multipartFile.getInputStream(),
          directory + multipartFile.getOriginalFilename(), multipartFile.getContentType(),
          multipartFile.getBytes().length);
    } catch (IOException e) {
      throw new FileException("Erro de IO: " + e.getMessage());
    }
  }

  private URL uploadFile(InputStream is, String fileName, String contentType, int length) {
    ObjectMetadata meta = new ObjectMetadata();
    meta.setContentType(contentType);
    meta.setContentLength(length);
    LOG.info("Iniciando upload");
    s3client.putObject(bucketS3, fileName, is, meta);
    LOG.info("Upload finalizado");
    return s3client.getUrl(bucketS3, fileName);
  }
}

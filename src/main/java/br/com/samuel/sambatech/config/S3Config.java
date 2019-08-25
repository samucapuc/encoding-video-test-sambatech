package br.com.samuel.sambatech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

  @Value("${bucket.videos.aws.s3}")
  private String bucketS3;

  @Value("${accessKey.acess.bucket}")
  private String accessKeyS3;

  @Value("${secretKey.acess.bucket}")
  private String secretKeyS3;

  @Value("${cloud.region.videos.aws.s3}")
  private String regionS3;

  @Bean
  public AmazonS3 s3client() {
    BasicAWSCredentials awsCred = new BasicAWSCredentials(accessKeyS3, secretKeyS3);
    AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(regionS3))
        .withCredentials(new AWSStaticCredentialsProvider(awsCred)).build();
    return s3client;
  }
}

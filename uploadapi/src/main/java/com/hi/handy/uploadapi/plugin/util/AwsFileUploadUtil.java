package com.hi.handy.uploadapi.plugin.util;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.InputStream;
import org.jivesoftware.util.JiveGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AwsFileUploadUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(AwsFileUploadUtil.class);

  private static String CONCIERGE_ENVIRONMENT = "concierge.environment";
  private static String ACCESS_KEY = "AKIAS7EXJMYWUZJLRIAM";
  private static String SECRET_KEY = "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";
  private static String BUCKET_NAME = "handy-concierge-chat";
  private static String DEFAULT_BUCKET_FILE_PATH = "staging";
  private static String FILE_PATH = "https://handy-concierge-chat.s3-ap-southeast-1.amazonaws.com/";

  private static AmazonS3 AMAZON_S3_CLIENT;

  static {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
    AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials));
    builder.setRegion(Regions.AP_SOUTHEAST_1.getName());
    AMAZON_S3_CLIENT = builder.build();
  }

  public static String upload(InputStream fileInputStream, String fileName, long fileSize,
      String contentType) {
    try {
      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentType(contentType);
      objectMetadata.setContentLength(fileSize);
      String key = getKey() + "/" + fileName;
      AMAZON_S3_CLIENT.putObject(new PutObjectRequest(BUCKET_NAME, key, fileInputStream, objectMetadata));
      return FILE_PATH + key;
    } catch (Exception e) {
      LOGGER.error("upload error", e);
      e.printStackTrace();
    }
    return null;
  }

  private static String getKey() {
    String env = JiveGlobals.getProperty(CONCIERGE_ENVIRONMENT);
    if (env != null &&
        (env.equals("production") || env.equals("product") || env.equals("prod")
            || env.equals("pro"))) {
      return "production";
    }
    return DEFAULT_BUCKET_FILE_PATH;
  }
}

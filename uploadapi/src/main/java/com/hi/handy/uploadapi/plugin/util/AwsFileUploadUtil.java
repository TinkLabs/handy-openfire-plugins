package com.hi.handy.uploadapi.plugin.util;


import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
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

  public static AmazonS3 Init() {
    AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));
    s3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
    return s3;
  }

  public static String upload(InputStream fileInputStream, String fileName, long fileSize,
      String contentType) {
    AmazonS3 s3 = Init();
    try {
      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentType(contentType);
      objectMetadata.setContentLength(fileSize);
      String key = getKey() + "/" + fileName;
      s3.putObject(new PutObjectRequest(BUCKET_NAME, key, fileInputStream, objectMetadata));
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

  public static S3Object download(String filePath) {
    AmazonS3 s3 = Init();
    return s3.getObject(new GetObjectRequest(BUCKET_NAME, filePath));
  }
}

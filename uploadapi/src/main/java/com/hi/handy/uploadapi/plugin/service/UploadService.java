package com.hi.handy.uploadapi.plugin.service;

import com.hi.handy.uploadapi.plugin.util.AwsFileUploadUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadService {

  private static final String FILE_FORM_NAME = "messageFile";

  public static final UploadService INSTANCE = new UploadService();

  private UploadService() {
  }

  public static UploadService getInstance() {
    return INSTANCE;
  }

  public String upload(HttpServletRequest req) throws IOException, ServletException {

//    String source = req.getParameter("source");
    MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement("");
    req.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);

    Part part = req.getPart(FILE_FORM_NAME);
    String disposition = part.getHeader("Content-Disposition");
    String suffix = disposition.substring(disposition.lastIndexOf("."), disposition.length() - 1);

    String filename = UUID.randomUUID() + suffix;
    InputStream is = part.getInputStream();

    String filePath = AwsFileUploadUtil.upload(is, filename, part.getSize(), part.getContentType());
    return filePath;
  }
}

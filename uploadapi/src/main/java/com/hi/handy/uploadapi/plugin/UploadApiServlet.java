package com.hi.handy.uploadapi.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hi.handy.uploadapi.plugin.exception.BusinessException;
import com.hi.handy.uploadapi.plugin.exception.ExceptionConst;
import com.hi.handy.uploadapi.plugin.model.BaseResultModel;
import com.hi.handy.uploadapi.plugin.service.UploadService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jivesoftware.admin.AuthCheckFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadApiServlet extends HttpServlet {

  private static final Logger LOGGER = LoggerFactory.getLogger(UploadApiServlet.class);

  private static final String SERVICE_URL = "uploadapi/upload";

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    AuthCheckFilter.addExclude(SERVICE_URL);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    resp.setHeader("Access-Control-Allow-Origin", "*");
    resp.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
    req.setCharacterEncoding("utf-8");
    resp.setCharacterEncoding("utf-8");
    resp.setContentType("application/json; charset=utf-8");

    BaseResultModel resultModel = new BaseResultModel();
    ObjectMapper mapper = new ObjectMapper();
    try {
      Object result = UploadService.getInstance().upload(req);
      resultModel.setData(result);
    } catch (BusinessException be) {
      LOGGER.error("upload have BusinessException", be);
      be.printStackTrace();
      resultModel.setSuccess(false);
      resultModel.setCode(be.getCode());
      resultModel.setMessage(be.getMessage());
    } catch (Exception e) {
      LOGGER.error("upload have Exception", e);
      e.printStackTrace();
      resultModel.setSuccess(false);
      resultModel.setCode(ExceptionConst.SYSTEM_ERROR);
      resultModel.setMessage(e.getMessage());
    }
    outputResult(resultModel, resp.getWriter(), mapper);
  }


  private void outputResult(BaseResultModel result, PrintWriter writer, ObjectMapper mapper) {
    if (mapper == null) {
      mapper = new ObjectMapper();
    }
    try {
      String json = mapper.writer().writeValueAsString(result);
      writer.write(json);
      writer.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public void destroy() {
    super.destroy();
    AuthCheckFilter.removeExclude(SERVICE_URL);
  }
}

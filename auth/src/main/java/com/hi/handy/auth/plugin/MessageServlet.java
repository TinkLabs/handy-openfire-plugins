package com.hi.handy.auth.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hi.handy.auth.plugin.exception.BusinessException;
import com.hi.handy.auth.plugin.exception.ExceptionConst;
import com.hi.handy.auth.plugin.model.BaseResultModel;
import com.hi.handy.auth.plugin.model.MessageModel;
import com.hi.handy.auth.plugin.parameter.MessageParameter;
import com.hi.handy.auth.plugin.service.MessageService;
import org.jivesoftware.admin.AuthCheckFilter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MessageServlet extends HttpServlet {

  private static final String SERVICE_URL = "message/list";

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    AuthCheckFilter.addExclude(SERVICE_URL);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws IOException {
    req.setCharacterEncoding("utf-8");
    resp.setCharacterEncoding("utf-8");
    resp.setContentType("application/json; charset=utf-8");
    BaseResultModel resultModel = new BaseResultModel();
    ObjectMapper mapper = new ObjectMapper();
    try {
      MessageParameter parameter = mapper.readValue(req.getInputStream(), MessageParameter.class);
      List<MessageModel> result = MessageService.getInstance().list(parameter);
      resultModel.setData(result);
    } catch (BusinessException be) {
      be.printStackTrace();
      resultModel.setSuccess(false);
      resultModel.setCode(be.getCode());
      resultModel.setMessage(be.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      resultModel.setSuccess(false);
      resultModel.setCode(ExceptionConst.SYSTEM_ERROR);
      resultModel.setMessage(e.getMessage());
    }
    outputResult(resultModel, resp.getWriter(), mapper);
  }


  private void outputResult(BaseResultModel result, PrintWriter writer, ObjectMapper mapper) {
    // TODO

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

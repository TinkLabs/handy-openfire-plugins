package com.hi.handy.group.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hi.handy.group.plugin.exception.BusinessException;
import com.hi.handy.group.plugin.exception.ExceptionConst;
import com.hi.handy.group.plugin.model.BaseResultModel;
import com.hi.handy.group.plugin.parameter.GroupParameter;
import com.hi.handy.group.plugin.service.GroupService;
import org.jivesoftware.admin.AuthCheckFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GroupRegisterServlet extends HttpServlet {
  private static final Logger LOGGER = LoggerFactory.getLogger(GroupRegisterServlet.class);
  private static final String SERVICE_URL = "group/register";

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    AuthCheckFilter.addExclude(SERVICE_URL);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setHeader("Access-Control-Allow-Origin","*");
    resp.setHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE");
    req.setCharacterEncoding("utf-8");
    resp.setCharacterEncoding("utf-8");
    resp.setContentType("application/json; charset=utf-8");
    BaseResultModel resultModel = new BaseResultModel();
    ObjectMapper mapper = new ObjectMapper();
    try {
      GroupParameter parameter = mapper.readValue(req.getInputStream(), GroupParameter.class);
      Object result = GroupService.getInstance().groupRegister(parameter);
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

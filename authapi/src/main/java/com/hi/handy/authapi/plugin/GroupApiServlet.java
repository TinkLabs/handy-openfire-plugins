package com.hi.handy.authapi.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import com.hi.handy.authapi.plugin.model.BaseResultModel;
import com.hi.handy.authapi.plugin.parameter.AuthParameter;
import com.hi.handy.authapi.plugin.parameter.GroupParameter;
import com.hi.handy.authapi.plugin.parameter.Relation;
import com.hi.handy.authapi.plugin.service.AuthService;
import com.hi.handy.authapi.plugin.service.GroupService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jivesoftware.admin.AuthCheckFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangxiutao
 * @mail xiutao.huang@tinklabs.com
 * @create 2019-06-27 14:44
 * @Description
 */
public class GroupApiServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupApiServlet.class);
    private static final String SERVICE_URL = "authapi/groupapi";

    // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";

    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // 去掉登录验证
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


        // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(req)) {
            // 如果不是则停止
            resultModel.setSuccess(false);
            resultModel.setCode(ExceptionConst.PARAMETER_ERROR);
            resultModel.setMessage("表单必须包含 enctype=multipart/form-data");
            outputResult(resultModel, resp.getWriter(), mapper);
            LOGGER.warn("表单必须包含 enctype=multipart/form-data");
            return;
        }


        try {
            //TODO 图片无法创建路径
            GroupParameter groupParameter = handleParameter(req);
//            GroupParameter parameter = mapper.readValue(req.getInputStream(), GroupParameter.class);
            Object result = GroupService.getInstance().groupRegister(groupParameter);
            resultModel.setSuccess(true);
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

    private GroupParameter handleParameterGet(HttpServletRequest req) {
        GroupParameter groupParameter = new GroupParameter();

        String displayName = req.getParameter("displayName");
        String type = req.getParameter("type");
        String welcomeMsg = req.getParameter("welcomeMessage");
        //relations参数的形式：id:name;id:name
        String relations = req.getParameter("relations");

        LOGGER.info("group create post parameter:"+displayName+" >"+type+" >"+welcomeMsg+" >"+relations);
        List<Relation> relations1 = new ArrayList<>();
        if(null!=relations){
            String relationArrays[] = relations.split(";");

            if(null!=relationArrays){
                for (String relationArray : relationArrays) {
                    String relationPropsp[] = relationArray.split(":");
                    if(null!=relationPropsp&&relationPropsp.length==2){
                        Relation re = new Relation();
                        re.setId(relationPropsp[0]);
                        re.setName(relationPropsp[1]);
                        relations1.add(re);
                    }else{
                        throw new BusinessException(ExceptionConst.PARAMETER_ERROR,"relations parameter is error!");
                    }

                }
            }else {
                throw new BusinessException(ExceptionConst.PARAMETER_ERROR,"relations parameter is error!");
            }

        }else{
            throw new BusinessException(ExceptionConst.PARAMETER_LOSE,"relations parameter is needed!");
        }
        groupParameter.setDisplayName(displayName);
        groupParameter.setType(type);
        groupParameter.setWelcomeMessage(welcomeMsg);
        groupParameter.setRelations(relations1);
        return groupParameter;
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

    public GroupParameter handleParameter(HttpServletRequest req){
        //TODO 无法创建路径


        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // 中文处理
        upload.setHeaderEncoding("UTF-8");

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
        String uploadPath = req.getServletContext().getRealPath("/") + File.separator + UPLOAD_DIRECTORY;


        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            LOGGER.info("upload path create: "+uploadPath);
            boolean flag = uploadDir.mkdirs();
            LOGGER.info("creted? "+flag);
        }
        String r = "";
        GroupParameter groupParameter = new GroupParameter();
        try {
            // 解析请求的内容提取文件数据
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(req);

            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                String displayName = null;
                String type = null;
                String welcomeMsg = null;
                //relations参数的形式：id:name;id:name
                String relations = null;
                LOGGER.info("formItems size:"+formItems.size());
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
//                        String fileName = new File(item.getName()).getName();
//                        String filePath = uploadPath + File.separator + fileName;
//                        File storeFile = new File(filePath);
//                        r += filePath;
                        // 保存文件到硬盘
//                        item.write(storeFile);
                    }else{

                        if("displayName".equals(item.getFieldName())){
                            displayName = item.getString("UTF-8");
                            continue;
                        }
                        if("type".equals(item.getFieldName())){
                            type = item.getString("UTF-8");
                            continue;
                        }
                        if("welcomeMessage".equals(item.getFieldName())){
                            welcomeMsg = item.getString("UTF-8");
                            continue;
                        }
                        if("relations".equals(item.getFieldName())){
                            relations = item.getString("UTF-8");
                            continue;
                        }
                    }
                }
                LOGGER.info("group create post parameter:"+displayName+" >"+type+" >"+welcomeMsg+" >"+relations);
                List<Relation> relations1 = new ArrayList<>();
                if(null!=relations){
                    String relationArrays[] = relations.split(";");
                    if(null!=relationArrays){
                        for (String relationArray : relationArrays) {
                            String relationPropsp[] = relationArray.split(":");
                            if(null!=relationPropsp&&relationPropsp.length==2){
                                Relation re = new Relation();
                                re.setId(relationPropsp[0]);
                                re.setName(relationPropsp[1]);
                                relations1.add(re);
                            }else{
                                throw new BusinessException(ExceptionConst.PARAMETER_ERROR,"relations parameter is error!");
                            }

                        }
                    }else {
                        throw new BusinessException(ExceptionConst.PARAMETER_ERROR,"relations parameter is error!");
                    }

                }else{
                    throw new BusinessException(ExceptionConst.PARAMETER_LOSE,"relations parameter is needed!");
                }
                groupParameter.setDisplayName(displayName);
                groupParameter.setType(type);
                groupParameter.setWelcomeMessage(welcomeMsg);
                groupParameter.setRelations(relations1);
            }
            return groupParameter;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new BusinessException(ExceptionConst.BUSINESS_ERROR,ex.getMessage());
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        AuthCheckFilter.removeExclude(SERVICE_URL);
    }}

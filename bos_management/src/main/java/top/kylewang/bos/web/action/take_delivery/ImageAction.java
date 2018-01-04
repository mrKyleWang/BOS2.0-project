package top.kylewang.bos.web.action.take_delivery;

import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import top.kylewang.bos.web.action.common.BaseAction;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Kyle.Wang
 * 2018/1/4 0004 19:32
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class ImageAction extends BaseAction<Object>{

    private File imgFile;
    private String imgFileFileName;
    private String imgFileContentType;

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }
    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }
    public void setImgFileContentType(String imgFileContentType) {
        this.imgFileContentType = imgFileContentType;
    }


    @Action(value = "image_upload",
            results = {@Result(name = "success",type = "json")})
    public String upload() throws IOException {
        System.out.println("文件:"+imgFile);
        System.out.println("文件名:"+imgFileFileName);
        System.out.println("文件类型:"+imgFileContentType);

        // 服务器保存路径(绝对路径)
        String savePath = ServletActionContext.getServletContext().getRealPath("/upload");
        // 用户访问路径(相对路径)
        String saveUrl = ServletActionContext.getRequest().getContextPath()+"/upload";
        System.out.println(saveUrl);
        // 生成随机图片名
        UUID uuid = UUID.randomUUID();
        // 文件扩展名
        String ext = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
        // 保存图片名
        String saveFileName = uuid+ext;
        // 保存图片
        FileUtils.copyFile(imgFile, new File(savePath +"/"+ saveFileName));
        // 向浏览器响应响应数据
        Map<String,Object> result  = new HashMap<>(4);
        result.put("error",0);
        result.put("url",saveUrl+"/"+saveFileName);
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
}




package top.kylewang.bos.web.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import top.kylewang.bos.domain.base.Standard;
import top.kylewang.bos.service.base.StandardService;

/**
 * @author Kyle.Wang
 * 2017/12/29 0029 11:46
 */
@Controller
@Scope("prototype")
@Actions
@Namespace("/")
@ParentPackage("struts-default")
public class StandardAction extends ActionSupport implements ModelDriven<Standard>{

    private Standard standard = new Standard();

    @Autowired
    private StandardService standardService;

    @Override
    public Standard getModel() {
        return standard;
    }


    @Action(value = "standard_save",
            results = {@Result(name = "success", type = "redirect",location = "./pages/base/standard.html")})
    public String sava(){
        standardService.save(standard);
        return SUCCESS;
    }
}

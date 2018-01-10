package top.kylewang.bos.web.action.system;

import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import top.kylewang.bos.domain.system.Menu;
import top.kylewang.bos.service.system.MenuService;
import top.kylewang.bos.web.action.common.BaseAction;

import java.util.List;

/**
 * @author Kyle.Wang
 * 2018/1/10 0010 21:20
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class MenuAction extends BaseAction<Menu>{

    @Autowired
    private MenuService menuService;


    @Action(value = "menu_list",results = {@Result(name = "success",type = "json")})
    public String list(){
        List<Menu> list = menuService.findAll();
        ActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }

    @Action(value = "menu_save",
            results = {@Result(name = "success",location = "./pages/system/menu.html",type = "redirect")})
    public String save(){
        menuService.save(model);
        return SUCCESS;
    }
}

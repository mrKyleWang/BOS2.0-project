package top.kylewang.bos.web.action;

import com.opensymphony.xwork2.ActionContext;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import top.kylewang.bos.domain.constants.Constants;
import top.kylewang.bos.domain.page.PageBean;
import top.kylewang.bos.domain.take_delivery.Promotion;
import top.kylewang.bos.web.action.common.BaseAction;

import javax.ws.rs.core.MediaType;

/**
 * @author Kyle.Wang
 * 2018/1/6 0006 10:46
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction extends BaseAction<Promotion>{


    @Action(value = "promotion_pageQuery",
            results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        //基于WebService获取bos_management 的活动列表数据信息
        PageBean pageBean = WebClient
                .create(Constants.BOS_MANAGEMENT_URL+"/bos/services/promotionService/pageQuery?page=" + page + "&rows=" + rows)
                .accept(MediaType.APPLICATION_JSON).get(PageBean.class);
        ActionContext.getContext().getValueStack().push(pageBean);
        return SUCCESS;
    }
}

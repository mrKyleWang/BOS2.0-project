package top.kylewang.bos.web.action;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import top.kylewang.bos.utils.SmsUtils;
import top.kylewang.bos.web.action.common.BaseAction;
import top.kylewang.crm.domain.Customer;

/**
 * @author Kyle.Wang
 * 2018/1/2 0002 21:24
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CustomerAction extends BaseAction<Customer>{

    @Action(value = "customer_sendSms")
    public String sendSms() throws Exception {
        // 手机号存在Customer对象
        String mobile = model.getTelephone();
        //生成短信验证码
        String randomCode = RandomStringUtils.randomNumeric(6);
        //将短信验证码保存到session
        ServletActionContext.getRequest().getSession().setAttribute(mobile,randomCode);
        System.out.println("短信验证码:"+randomCode);
        //调用工具类发送短信
        // TODO 启用短信服务
        //String responseCode = SmsUtils.sendCode(mobile, randomCode);
        String responseCode = "000000";
        System.out.println("响应结果代码:"+responseCode);
        if(responseCode.equals(SmsUtils.SEND_SUCCESS)){
            //发送成功
            return NONE;
        }else{
            //发送失败
            throw new RuntimeException("短信发送失败, 信息码:"+responseCode);
        }
    }
}

package top.kylewang.bos.web.action.common;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author Kyle.Wang
 * 2017/12/30 0030 14:49
 */
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>{

    protected T model;

    @Override
    public T getModel() {
        return model;
    }
}

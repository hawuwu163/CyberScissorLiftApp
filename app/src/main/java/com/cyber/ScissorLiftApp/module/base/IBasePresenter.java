package com.cyber.ScissorLiftApp.module.base;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.module.base
 * @date 2018/12/28 14:40
 * @Description: presenter底层接口设计
 */
public interface IBasePresenter {
    /**
     * 刷新数据逻辑
     */
    void doRefresh();

    /**
     * 显示错误逻辑
     */
    void doShowError(int val);


}

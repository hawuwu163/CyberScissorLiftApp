package com.cyber.ScissorLiftApp.module.base;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.module.base
 * @date 2018/12/28 14:40
 * @Description:
 */
public interface IBasePresenter {
    /**
     * 刷新数据
     */
    void doRefresh();

    /**
     * 显示网络错误
     */
    void doShowNetError();
}

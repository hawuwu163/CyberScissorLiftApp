package com.cyber.ScissorLiftApp.module.base;

import com.uber.autodispose.AutoDisposeConverter;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.module.base
 * @date 2018/12/28 14:41
 * @Description:
 */
public interface IBaseView<T> {

    /**
     * 显示加载动画
     */
    void onShowLoading();

    /**
     * 隐藏加载
     */
    void onHideLoading();

    /**
     * 显示网络错误
     */
    void onShowNetError();

    /**
     * 设置 presenter
     */
    void setPresenter(T presenter);

    /**
     * 绑定生命周期
     */
    <X> AutoDisposeConverter<X> bindAutoDispose();
}

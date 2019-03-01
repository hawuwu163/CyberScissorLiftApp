package com.cyber.ScissorLiftApp.module.base;

import com.uber.autodispose.AutoDisposeConverter;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.module.base
 * @date 2018/12/28 14:41
 * @Description: 显示非列表页面动画相关的接口,设置presenter相关的接口
 */
public interface IBaseView<T> {

    /**
     * 显示加载动画
     */
    void onShowLoading();

    /**
     * 隐藏加载动画
     */
    void onHideLoading();

    /**
     * 显示错误动画
     */
    void onError(int val);

    /**
     * 设置 presenter
     */
    void setPresenter(T presenter);

    /**
     * 绑定生命周期
     */
    <X> AutoDisposeConverter<X> bindAutoDispose();
}
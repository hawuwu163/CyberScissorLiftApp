package com.cyber.ScissorLiftApp.module.base;

import java.util.List;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.module.base
 * @date 2019/1/17 16:08
 * @Description: 显示列表页面动画相关的接口,设置presenter相关的接口
 */
public interface IBaseListView<T> extends IBaseView<T> {

    /**
     * 设置适配器
     */
    void onSetAdapter(List<?> list);

    /**
     * 显示加载完毕动画
     */
    void onShowNoMore();

}

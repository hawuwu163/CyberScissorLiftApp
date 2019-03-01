package com.cyber.ScissorLiftApp.module.parameter;

import com.cyber.ScissorLiftApp.module.base.IBaseListView;
import com.cyber.ScissorLiftApp.module.base.IBasePresenter;

import java.util.List;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.module.bean
 * @date 2019/1/17 16:16
 * @Description:
 */

public interface IParameterList {

    /** View:
     * onLoadData(); 请求数据
     * extends:
     * onLoadingFinished();显示加载完毕动画
     * onSetAdapter(List<?> list);设置适配器
     */
    interface View extends IBaseListView<Presenter> {

        /**
         * 请求数据
         */
        void onLoadData(String... arg);
    }

    interface Presenter extends IBasePresenter {

        /**
         * 请求数据
         */
        void doLoadData(String jsonList,String mode,String ...arg);

        /**
         * 设置适配器
         */
        void doSetAdapter(List<ParameterIntegerBean> list);

        /**
         * 显示没有更多
         */
        void doShowNoMore();

        boolean isEditable();
    }
}

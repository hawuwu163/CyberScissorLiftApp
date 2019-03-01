package com.cyber.ScissorLiftApp.module.bluetooth;

import com.cyber.ScissorLiftApp.module.base.IBaseListView;
import com.cyber.ScissorLiftApp.module.base.IBasePresenter;

import java.util.List;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.module.bluetooth
 * @date 2019/2/27 10:44
 * @Description:
 */
public interface IBleList {
    interface View extends IBaseListView<Presenter> {
        void onLoadData();
    }

    interface Presenter extends IBasePresenter {
        void doLoadData();

        void doSetAdaper();

        /**
         * 显示没有更多
         */
        void doShowNoMore();

        void addDevice(BleDevice bleDevice);
    }
}

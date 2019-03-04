package com.cyber.ScissorLiftApp.adapter;

import android.support.annotation.NonNull;

import com.cyber.ScissorLiftApp.binder.BleDeviceViewBinder;
import com.cyber.ScissorLiftApp.binder.LoadingEndViewBinder;
import com.cyber.ScissorLiftApp.binder.LoadingViewBinder;
import com.cyber.ScissorLiftApp.binder.MainOptionViewBinder;
import com.cyber.ScissorLiftApp.binder.ParameterListViewBinder;
import com.cyber.ScissorLiftApp.module.bean.LoadingBean;
import com.cyber.ScissorLiftApp.module.bean.LoadingEndBean;
import com.cyber.ScissorLiftApp.module.bluetooth.BleDevice;
import com.cyber.ScissorLiftApp.module.mainOption.MainOption;
import com.cyber.ScissorLiftApp.module.parameter.ParameterIntegerBean;
import com.cyber.ScissorLiftApp.module.parameter.ParameterListPresenter;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.adapter
 * @date 2019/2/19 13:07
 * @Description:
 */
public class Register {
    public static void registerParameterListItem(@NonNull MultiTypeAdapter adapter, ParameterListPresenter presenter) {
        adapter.register(ParameterIntegerBean.class, new ParameterListViewBinder(presenter));
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }
    public static void registerBleListItem(@NonNull MultiTypeAdapter adapter) {
        adapter.register(BleDevice.class, new BleDeviceViewBinder());
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    public static void registerMainOptionGridItem(@NonNull MultiTypeAdapter adapter) {
        adapter.register(MainOption.class, new MainOptionViewBinder());
    }
}

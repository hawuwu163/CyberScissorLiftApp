package com.cyber.ScissorLiftApp;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.cyber.ScissorLiftApp.module.bluetooth.BleDevice;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp
 * @date 2018/12/28 16:53
 * @Description: 继承MultiDexApplication来使App支持64k以上方法数量，注意要在AndroidManifest.xml的Application里增加android:name=".InitApp"
 */
public class InitApp extends MultiDexApplication {
    public static Context AppContext;

    public static BleDevice ConnectedBleDevice;
    /**
     * 全局App初始化
     */
    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();

//        initTheme();

        if (BuildConfig.DEBUG) {
            SdkManager.initStetho(this);
            SdkManager.initLeakCanary(this);
        }
    }

    public static BleDevice getConnectedBleDevice() {
        return ConnectedBleDevice;
    }

    public static void setConnectedBleDevice(BleDevice connectedBleDevice) {
        ConnectedBleDevice = connectedBleDevice;
    }
}
